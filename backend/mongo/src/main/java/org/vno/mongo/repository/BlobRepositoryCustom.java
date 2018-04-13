package org.vno.mongo.repository;

import org.vno.mongo.domain.Blob;

/**
 * @author kk
 */
public interface BlobRepositoryCustom {
    Blob findWithMaxId();
}
