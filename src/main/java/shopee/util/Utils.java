package shopee.util;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;

public class Utils {
	private static final int DEFAULT_TIMEOUT = 15000;

	public static HttpResponse executeRequest( Request request ) {
		return executeRequest( request, DEFAULT_TIMEOUT );
	}

	public static HttpResponse executeRequest( Request request, int timeout ) {
		return executeRequest( request, timeout, timeout );
	}

	public static HttpResponse executeRequest( Request request, int connectTimeout, int socketTimeout ) {
		try {
			HttpResponse response = request.connectTimeout( connectTimeout ).socketTimeout( socketTimeout ).execute().returnResponse();

			int status = response.getStatusLine().getStatusCode();

			if ( status != HttpStatus.SC_OK ) {
				throw new IllegalStateException( "Response status: " + status + ", request: " + request );

			}

			return response;

		} catch ( IOException e ) {
			throw new RuntimeException( e );

		}
	}

	public static String getEntityAsString( Request request ) {
		return getEntityAsString( request, CharEncoding.UTF_8 );
	}

	public static String getEntityAsString( Request request, int timeout ) {
		return getEntityAsString( request, CharEncoding.UTF_8, timeout );
	}

	public static String getEntityAsString( Request request, int connectTimeout, int socketTimeout ) {
		return getEntityAsString( request, CharEncoding.UTF_8, connectTimeout, socketTimeout );
	}

	public static String getEntityAsString( Request request, String charset ) {
		return getEntityAsString( request, charset, DEFAULT_TIMEOUT );
	}

	public static String getEntityAsString( Request request, String charset, int timeout ) {
		return getEntityAsString( request, charset, timeout, timeout );
	}

	public static String getEntityAsString( Request request, String charset, int connectTimeout, int socketTimeout ) {
		try {
			return EntityUtils.toString( executeRequest( request, connectTimeout, socketTimeout ).getEntity(), charset );

		} catch ( ParseException | IOException e ) {
			throw new RuntimeException( e );

		}
	}

	public static String getResourceAsString( String path ) {
		try {
			return IOUtils.toString( Utils.class.getResource( path ), CharEncoding.UTF_8 );

		} catch ( IOException e ) {
			throw new RuntimeException( "Path: " + path, e );
		}
	}
}