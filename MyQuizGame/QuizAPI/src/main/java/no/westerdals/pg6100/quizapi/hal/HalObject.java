package no.westerdals.pg6100.quizapi.hal;

import io.swagger.annotations.ApiModelProperty;

public class HalObject {

    @ApiModelProperty("HAL links")
    public HalLinkSet _links;
}
