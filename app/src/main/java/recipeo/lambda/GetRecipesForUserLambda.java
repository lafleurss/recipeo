package recipeo.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.GetCategoriesForUserRequest;
import recipeo.activity.requests.GetRecipeRequest;
import recipeo.activity.requests.GetRecipesForUserRequest;
import recipeo.activity.results.GetRecipeResult;
import recipeo.activity.results.GetRecipesForUserResult;

public class GetRecipesForUserLambda extends LambdaActivityRunner<GetRecipesForUserRequest, GetRecipesForUserResult>
        implements RequestHandler<LambdaRequest<GetRecipesForUserRequest>, LambdaResponse> {
    private final Logger log = LogManager.getLogger();

    /**
     * @param input   The Lambda Function input
     * @param context The Lambda execution environment context object.
     * @return a LambdaResponse
     */
    @Override
    public LambdaResponse handleRequest(LambdaRequest<GetRecipesForUserRequest> input, Context context) {
        log.info("handleRequest");

        return super.runActivity(
                () -> input.fromPath(path ->
                        GetRecipesForUserRequest.builder()
                                .withUserId(path.get("userId"))
                                .build()),
                (request, serviceComponent) ->
                        serviceComponent.provideGetRecipesForUserActivity().handleRequest(request)
        );
    }
}