package aws.retrospective.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class AmazonS3Service {

    private final S3Presigner presigner;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public String getPresignedUrl(String filename) {
        GetObjectRequest getObjectAclRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(filename)
            .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(5)) // 제한 시간 5분
            .getObjectRequest(getObjectAclRequest)
            .build();

        PresignedGetObjectRequest presignedGetObjectRequest = presigner
            .presignGetObject(getObjectPresignRequest);

        String url = presignedGetObjectRequest.url().toString();

        presigner.close();
        return url;
    }

    public String getPresignedUrlForUpload(String filename) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(filename)
            .build();

        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(5)) // 제한 시간 5분
            .putObjectRequest(putObjectRequest)
            .build();

        PresignedPutObjectRequest presignedPutObjectRequest = presigner
            .presignPutObject(putObjectPresignRequest);

        String url = presignedPutObjectRequest.url().toString();

        presigner.close();
        return url;
    }
}
