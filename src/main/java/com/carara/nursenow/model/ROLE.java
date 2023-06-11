package com.carara.nursenow.model;

import lombok.experimental.FieldNameConstants;


@FieldNameConstants(onlyExplicitlyIncluded = true)
public enum ROLE {

    @FieldNameConstants.Include
    CAREGIVER,
    @FieldNameConstants.Include
    CARERECIVIER

}
