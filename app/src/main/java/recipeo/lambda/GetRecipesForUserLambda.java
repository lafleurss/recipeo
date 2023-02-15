package recipeo.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.GetRecipesForUserRequest;
import recipeo.activity.results.GetRecipesForUserResult;
import recipeo.models.RecipeFilter;

public class GetRecipesForUserLambda extends LambdaActivityRunner<GetRecipesForUserRequest, GetRecipesForUserResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetRecipesForUserRequest>, LambdaResponse> {
    private final Logger log = LogManager.getLogger();

    /**
     * @param input   The Lambda Function input
     * @param context The Lambda execution environment context object.
     * @return a LambdaResponse
     */
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetRecipesForUserRequest> input, Context context) {
        log.info("Received GetRecipesForUserLambda {}" , input);

        GetRecipesForUserRequest unauthenticatedRequest = input.fromQuery(i ->
                GetRecipesForUserRequest.builder()
                        .withFilterType(RecipeFilter.valueOf(i.get("filterType")))
                        .build());


        return super.runActivity(() -> input.fromUserClaims(claims ->
                        GetRecipesForUserRequest.builder()
                                .withUserId(claims.get("email"))
                                .withFilterType(unauthenticatedRequest.getFilterType())
                                .build()), (request, serviceComponent) ->
                        serviceComponent.provideGetRecipesForUserActivity().handleRequest(request)
        );
    }
}
