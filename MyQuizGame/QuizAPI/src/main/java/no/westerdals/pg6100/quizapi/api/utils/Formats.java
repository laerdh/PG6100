package no.westerdals.pg6100.quizapi.api.utils;

public interface Formats {

    String BASE_JSON = "application/json; charset=UTF-8";

    //note the "vnd." (which starts for "vendor") and the
    // "+json" (ie, treat it having json structure)
    String V1_JSON = "application/vnd.pg6100.quiz+json; charset=UTF-8; version=1";
}
