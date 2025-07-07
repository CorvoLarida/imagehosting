FROM minio/mc:latest

SHELL ["/bin/bash", "-c"]

ENTRYPOINT [""]

CMD mc config host add --api=s3v4 myminio http://minio:9000 ${MINIO_ROOT_USER} ${MINIO_ROOT_PASSWORD}; \
    mc mb myminio/${MINIO_STORAGE_BUCKET_NAME}; \
    mc anonymous set download myminio/${MINIO_STORAGE_BUCKET_NAME}; \
    exit 0;