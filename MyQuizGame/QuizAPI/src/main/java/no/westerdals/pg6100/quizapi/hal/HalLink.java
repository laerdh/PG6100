package no.westerdals.pg6100.quizapi.hal;

import io.swagger.annotations.ApiModelProperty;

public class HalLink {

    @ApiModelProperty("URL of the link")
    public String href;

    public HalLink() {}

    public HalLink(String href) {
        this.href = href;
    }
}
