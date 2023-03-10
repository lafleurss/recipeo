package recipeo.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.GetRecipesForUserInCategoryRequest;
import recipeo.activity.results.GetRecipesForUserInCategoryResult;

public class GetRecipesForUserInCategoryLambda
        extends LambdaActivityRunner<GetRecipesForUserInCategoryRequest, GetRecipesForUserInCategoryResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetRecipesForUserInCategoryRequest>, LambdaResponse> {
    private final Logger log = LogManager.getLogger();

    /**
     * @param input   The Lambda Function input
     * @param context The Lambda execution environment context object.
     * @return a LambdaResponse
     */
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetRecipesForUserInCategoryRequest> input,
                                        Context context) {
        log.info("Received GetRecipesForUserInCategoryLambda {}" , input);

        GetRecipesForUserInCategoryRequest unauthenticatedRequest = input.fromPath(i ->
                GetRecipesForUserInCategoryRequest.builder()
                        .withCategoryName(i.get("categoryName"))
                        .build());


        return super.runActivity(() -> input.fromUserClaims(claims ->
                GetRecipesForUserInCategoryRequest.builder()
                                .withUserId(claims.get("email"))
                                .withCategoryName(unauthenticatedRequest.getCategory())
                                .build()), (request, serviceComponent) ->
                        serviceComponent.provideGetRecipesForUserInCategoryActivity().handleRequest(request)
        );
    }
}
