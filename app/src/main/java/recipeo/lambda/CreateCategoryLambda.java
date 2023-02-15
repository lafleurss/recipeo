package recipeo.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.CreateCategoryRequest;
import recipeo.activity.results.CreateCategoryResult;

public class CreateCategoryLambda extends LambdaActivityRunner<CreateCategoryRequest, CreateCategoryResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateCategoryRequest>, LambdaResponse> {
    private final Logger log = LogManager.getLogger();

    /**
     * @param input   The Lambda Function input
     * @param context The Lambda execution environment context object.
     * @return a LambdaResponse
     */
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateCategoryRequest> input, Context context) {
        log.info("Received CreateCategoryLambda {}" , input);

        CreateCategoryRequest unauthenticatedRequest = input.fromBody(CreateCategoryRequest.class);

        return super.runActivity(() -> input.fromUserClaims(claims ->
                        CreateCategoryRequest.builder()
                                .withCategoryName(unauthenticatedRequest.getCategoryName())
                                .withCategoryDescription(unauthenticatedRequest.getCategoryDescription())
                                .withUserId(claims.get("email"))
                                .build()), (request, serviceComponent) ->
                        serviceComponent.provideCreateCategoryActivity().handleRequest(request)
        );
    }
}
