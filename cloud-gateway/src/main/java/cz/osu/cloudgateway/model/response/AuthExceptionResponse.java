package cz.osu.cloudgateway.model.response;

public record AuthExceptionResponse(int errorCode,String errorMsg,String timeStamp) {
}

