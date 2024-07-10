package konkuk.tourkk.chons.global.common.photo;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PhotoConfig {

    @Value("${ncp.bucket.end-point}")
    private String endPoint;

    @Value("${ncp.bucket.region-name}")
    private String regionName;

    @Value("${ncp.bucket.access-key}")
    private String accessKey;

    @Value("${ncp.bucket.secret-key}")
    private String secretKey;

    @Value("${ncp.bucket.name}")
    private String bucketName;

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }
}
