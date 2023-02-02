package recipeo.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.UpdateCategoryRequest;
import recipeo.activity.results.UpdateCategoryResult;

public class UpdateCategoryLambda extends LambdaActivityRunner<UpdateCategoryRequest, UpdateCategoryResult>
        implements RequestHandler<AuthenticatedLambdaRequest<UpdateCategoryRequest>, LambdaResponse> {
    private final Logger log = LogManager.getLogger();

    /**
     * @param input   The Lambda Function input
     * @param context The Lambda execution environment context object.
     * @return a LambdaResponse
     */
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<UpdateCategoryRequest> input, Context context) {
        log.info("handleRequest");

        UpdateCategoryRequest unauthenticatedRequest = input.fromBody(UpdateCategoryRequest.class);

        return super.runActivity(() -> input.fromUserClaims(claims ->
                        UpdateCategoryRequest.builder()
                                .withCategoryName(unauthenticatedRequest.getCategoryName())
                                .withCategoryDescription(unauthenticatedRequest.getCategoryDescription())
                                .withUserId(claims.get("email"))
                                .build()), (request, serviceComponent) ->
                        serviceComponent.provideUpdateCategoryActivity().handleRequest(request)
        );
    }
}
