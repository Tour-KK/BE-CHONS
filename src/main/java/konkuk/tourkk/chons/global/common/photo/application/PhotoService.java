package konkuk.tourkk.chons.global.common.photo.application;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.transaction.Transactional;
import konkuk.tourkk.chons.global.common.photo.exception.PhotoException;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PhotoService {

    private final AmazonS3 s3;

    @Value("${ncp.bucket.end-point}")
    private String endPoint;

    @Value("${ncp.bucket.name}")
    private String bucketName;

    public static final String HOUSE_BUCKET_FOLDER = "house/";

    public static final String REVIEW_BUCKET_FOLDER = "review/";

    public List<String> savePhotos(List<MultipartFile> photos, String folderName) {
        log.info("응?1");
        List<String> photoUrls = new ArrayList<>();
        for (MultipartFile photo : photos) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(photo.getSize());
            objectMetadata.setContentType(photo.getContentType());
            String photoName = UUID.randomUUID().toString();
            try {
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName + photoName, photo.getInputStream(), objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead);
                s3.putObject(putObjectRequest);
            } catch (SdkClientException | IOException e) {
                throw new PhotoException(ErrorCode.FAILED_TO_SAVE_PHOTO, e.getMessage());
            }

            photoUrls.add(endPoint + "/" + folderName + photoName);
        }

        return photoUrls;
    }

    public void deleteReviewPhotos(List<String> photos) {
        for (String url : photos) {
            int index = url.indexOf(bucketName + "/");
            String photoName = url.substring(index + bucketName.length() + 1);
            try {
                s3.deleteObject(bucketName, photoName);
            } catch (AmazonS3Exception e) {
                throw new PhotoException(ErrorCode.FAILED_TO_SAVE_PHOTO, e.getMessage());
            }
        }
    }
}
