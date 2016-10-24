/**
  * Created by stanikol on 10/21/16.
  */
package filters

import javax.inject.{Inject, Singleton}

import akka.stream.Materializer
import play.api.Logger
import play.api.http.DefaultHttpFilters
import play.api.mvc.{Filter, RequestHeader, Result}
import play.filters.gzip.GzipFilter

import scala.concurrent.{ExecutionContext, Future}

// To control which responses are and aren’t implemented, use the shouldGzip parameter,
// which accepts a function of a request header and a response header to a boolean.
//      new GzipFilter(shouldGzip = (request, response) =>
//           response.body.contentType.exists(_.startsWith("text/html")))



@Singleton
class LoggingFilter @Inject() (implicit val mat: Materializer, ec: ExecutionContext) extends Filter {

  def apply(nextFilter: RequestHeader => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {

    val startTime = System.currentTimeMillis

    nextFilter(requestHeader).map { result =>

      val endTime = System.currentTimeMillis
      val requestTime = endTime - startTime

      Logger.info(s"${requestHeader.method} ${requestHeader.uri} took ${requestTime}ms and returned ${result.header.status}")

      result.withHeaders("Request-Time" -> requestTime.toString)
    }
  }
}

// The Filters class can either be in the root package,
// or if it has another name or is in another package,
// needs to be configured using play.http.filters
// in application.conf:
//          play.http.filters = "filters.MyFilters"
class Filters @Inject() (gzipFilter: GzipFilter, loggingFilter: LoggingFilter)
  extends DefaultHttpFilters(gzipFilter, loggingFilter)