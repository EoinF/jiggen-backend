package com.github.eoinf.jiggen

import com.github.eoinf.jiggen.config.JiggenConfig
import com.github.eoinf.jiggen.data.S3File
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.ResponseTransformer
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.HeadObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.S3Exception
import java.io.File
import java.nio.file.Paths

@Service
class S3BucketService(jiggenConfig: JiggenConfig) {
    private val bucket: String = jiggenConfig.bucketName!!
    private val region = Region.EU_WEST_1
    private val folder: String = jiggenConfig.bucketFolder!!

    private val s3 = S3Client.builder().region(region).build()

    fun getFileStream(key: String): S3File {
        val bucketKey = "$folder/$key"
        try {
            val getResponseStream = s3.getObject(
                GetObjectRequest.builder().bucket(this.bucket).key(bucketKey).build(),
                ResponseTransformer.toInputStream()
            )
            return S3File(getResponseStream, getResponseStream.response().contentLength())
        } catch (e: S3Exception) {
            throw Exception(e.awsErrorDetails().rawResponse().asUtf8String())
        }
    }

    fun downloadToFile(key: String, filePath: String): Boolean {
        val bucketKey = "$folder/$key"
        try {
            s3.getObject(
            GetObjectRequest.builder().bucket(this.bucket).key(bucketKey).build(),
            ResponseTransformer.toFile(File(filePath)))
            return true
        } catch (e: S3Exception) {
            return false
        }
    }

    fun putFile(key: String, filePath: String) {
        val bucketKey = "$folder/$key"
        try {
            s3.putObject(
                PutObjectRequest.builder().bucket(this.bucket).key(bucketKey).build(),
                Paths.get(filePath)
            )
        } catch (e: S3Exception) {
            throw Exception(e.awsErrorDetails().rawResponse().asUtf8String())
        }
    }

    fun objectExists(key: String): Boolean {
        val bucketKey = "$folder/$key"
        try {
            s3.headObject(
                HeadObjectRequest.builder().bucket(this.bucket).key(bucketKey).build()
            )
            return true
        } catch (e: S3Exception) {
            if (e.statusCode() == 404) {
                return false
            } else {
                throw Exception(e.awsErrorDetails().rawResponse().asUtf8String())
            }
        }
    }
}