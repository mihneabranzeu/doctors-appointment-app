package com.federatedlearningplatform.fl_tensorflow

import android.util.Log
import com.federatedlearningplatform.fl_tensorflow.helpers.assertIntsEqual
import com.google.gson.Gson
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.MappedByteBuffer
import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.withLock
import kotlin.concurrent.write

class FLClient<X: Any, Y: Any>(
    tfliteFileBuffer: MappedByteBuffer,
    val layersSizes: IntArray,
    val spec: SampleSpec<X, Y>
){
    val interpreter = Interpreter(tfliteFileBuffer)
    val trainingSamples = mutableListOf<Sample<X, Y>>()
    val testSamples = mutableListOf<Sample<X, Y>>()
    val trainSampleLock = ReentrantReadWriteLock()
    val testSampleLock = ReentrantReadWriteLock()
    val interpreterLock = ReentrantLock()

    /**
     * Adds a sample for training or testing
     */
    fun addSample(
        dataPoint: X, label: Y, isTraining: Boolean
    ) {
        val samples = if (isTraining) trainingSamples else testSamples
        val lock = if (isTraining) trainSampleLock else testSampleLock
        lock.write {
            samples.add(Sample(dataPoint, label))
        }
    }

    /**
     * Obtain the model parameters from the interpreter.
     */
    fun getParameters(): List<ByteArray> {
        val inputs: Map<String, Any> = FakeNonEmptyMap()
        val outputs = emptyParameterMap()

        // Run the signature
        interpreter.runSignature(inputs, outputs, "parameters")

        Log.i(TAG, "Raw weights: $outputs.")
//        return parametersFromMap(outputs)
        val gson = Gson()
        val json = gson.toJson(outputs)
        Log.i(TAG, "Json: $json")
        return parametersFromMap(outputs)
    }

    fun fit(
        epochs: Int = 1, batchSize: Int = 32, lossCallback: ((List<Float>) -> Unit)? = null
    ) {
        Log.d(TAG, "Starting to train for $epochs epochs with batch size $batchSize")
        return trainSampleLock.write {
            (1..epochs).map {
                val losses = trainOneEpoch(batchSize)
                Log.d(TAG, "Epoch $it: losses = $losses.")
                lossCallback?.invoke(losses)
                losses.average()
            }
        }
    }

    fun evaluate(): Pair<Float, Float> {
        val result = testSampleLock.read {
            val dataPoints = testSamples.map { it.dataPoint }
            val logits = inference(spec.convertX(dataPoints))
            spec.loss(testSamples, logits)to spec.accuracy(testSamples, logits)
        }
        Log.d(TAG, "Evaluate loss & accuracy: $result.")
        return result
    }

    fun inference(x: Array<X>): Array<Y> {
        val inputs = mapOf("x" to x)
        val logits = spec.emptyY(x.size)
        val outputs = mapOf("logits" to logits)
        interpreterLock.withLock {
            interpreter.runSignature(inputs, outputs, "infer")
        }
        return logits
    }

    private fun trainOneEpoch(batchSize: Int): List<Float> {
        if (trainingSamples.isEmpty()) {
            Log.d(TAG, "No training samples available.")
            return listOf()
        }

        trainingSamples.shuffle()
        var k = 0;
        return trainingBatches(Integer.min(batchSize, trainingSamples.size)).map {
            Log.d(TAG, "Training batch $k")
            k += 1
            val dataPoints = it.map { sample -> sample.dataPoint }
            val labels = it.map {sample -> sample.label }
            training(spec.convertX(dataPoints), spec.convertY(labels))
        }.toList()
    }

    private fun trainingBatches(trainBatchSize: Int): Sequence<List<Sample<X, Y>>> {
        return sequence {
            var nextIndex = 0

            while (nextIndex < trainingSamples.size) {
                val fromIndex = nextIndex
                nextIndex += trainBatchSize

                val batch = if (nextIndex >= trainingSamples.size) {
                    trainingSamples.subList(
                        trainingSamples.size - trainBatchSize, trainingSamples.size
                    )
                } else {
                    trainingSamples.subList(fromIndex, nextIndex)
                }

                yield(batch)
            }
        }
    }

    private fun training(
        bottlenecks: Array<X>, labels: Array<Y>
    ): Float {
        val inputs = mapOf<String, Any>(
            "x" to bottlenecks,
            "y" to labels,
        )
        val loss = FloatBuffer.allocate(1)
        val outputs = mapOf<String, Any>(
            "loss" to loss,
        )
        interpreterLock.withLock {
            interpreter.runSignature(inputs, outputs, "train")
        }
        return loss.get(0)
    }

    private fun emptyParameterMap(): Map<String, Any> {
        return layersSizes.mapIndexed { index, size -> "a$index" to ByteBuffer.allocate(size) }
            .toMap()
    }

    private fun ByteBuffer.toByteArray(): ByteArray {
        val array = ByteArray(remaining())
        get(array)
        return array
    }
    fun parametersFromMap(map: Map<String, Any>): List<ByteArray> {
        assertIntsEqual(layersSizes.size, map.size)
        val buffers = (0 until map.size).map {
            val buffer = map["a$it"] as ByteBuffer
            buffer.rewind()
            buffer.toByteArray()
        }

        Log.d(TAG, "Buffers size: ${buffers.size}")

//        for (buffer in buffers) {
//            while (buffer.hasRemaining()) {
//                val value = buffer.float
//               Log.d(TAG, "Value: $value")
//            }
//            buffer.rewind()
//        }
        return buffers
    }

    fun updateParameters(parameters: List<ByteArray> ){
        val outputs = emptyParameterMap()
        val inputs = parametersToMap(parameters)
        interpreterLock.withLock {
            interpreter.runSignature(inputs, outputs, "restore")
        }
    }

    private fun parametersToMap(parameters: List<ByteArray>): Map<String, Any> {
        assertIntsEqual(layersSizes.size, parameters.size)
        return parameters.mapIndexed { index, bytes -> "a$index" to ByteBuffer.wrap(bytes) }.toMap()
    }

    fun clearSamples() {
        trainingSamples.clear()
        testSamples.clear()
    }

    companion object {
        private const val TAG = "FLClient"
    }

}

/**
 * A simple representation of a data point and its label.
 */
data class Sample<X, Y>(val dataPoint: X, val label: Y);

class FakeNonEmptyMap<K, V> : HashMap<K, V>() {
    override fun isEmpty(): Boolean {
        return false
    }
}