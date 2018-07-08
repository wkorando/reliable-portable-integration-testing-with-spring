package com.bk.movie.client;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;

public class CORSExtension extends ResponseTransformer {

	@Override
	public String getName() {
		return "CORSExtension";
	}

	@Override
	public Response transform(Request request, Response response, FileSource files, Parameters parameters) {
		HttpHeaders httpHeaders = response.getHeaders();
		httpHeaders = httpHeaders.plus(new HttpHeader("access-control-allow-origin", "*"));
		return new Response( //
				response.getStatus(), //
				response.getStatusMessage(), //
				response.getBody(), //
				httpHeaders, //
				response.wasConfigured(), //
				response.getFault(), //
				response.getInitialDelay(), //
				response.getChunkedDribbleDelay(), //
				response.isFromProxy());
	}

}
