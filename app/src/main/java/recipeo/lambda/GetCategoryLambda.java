package recipeo.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.GetCategoryRequest;
import recipeo.activity.results.GetCategoryResult;

public class GetCategoryLambda extends LambdaActivityRunner<GetCategoryRequest, GetCategoryResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetCategoryRequest>, LambdaResponse> {
    private final Logger log = LogManager.getLogger();

    /**
     * @param input   The Lambda Function input
     * @param context The Lambda execution environment context object.
     * @return a LambdaResponse
     */
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetCategoryRequest> input, Context context) {
        log.info("handleRequest");

        GetCategoryRequest unauthenticatedRequest = input.fromPath(i ->
                GetCategoryRequest.builder()
                        .withCategoryName(i.get("categoryName"))
                        .build());


        return super.runActivity(() -> input.fromUserClaims(claims ->
                        GetCategoryRequest.builder()
                                .withCategoryName(unauthenticatedRequest.getCategoryName())
                                .withUserId(claims.get("email"))
                                .build()), (request, serviceComponent) ->
                        serviceComponent.provideGetCategoryActivity().handleRequest(request)
        );
    }
}
