package recipeo.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.DeleteRecipeRequest;
import recipeo.activity.results.DeleteRecipeResult;

public class DeleteRecipeLambda extends LambdaActivityRunner<DeleteRecipeRequest, DeleteRecipeResult>
    implements RequestHandler<AuthenticatedLambdaRequest<DeleteRecipeRequest>, LambdaResponse> {
    private final Logger log = LogManager.getLogger();

    /**
     * @param input   The Lambda Function input
     * @param context The Lambda execution environment context object.
     * @return a LambdaResponse
     */
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<DeleteRecipeRequest> input, Context context) {
        log.info("handleRequest");

        DeleteRecipeRequest unauthenticatedRequest = input.fromPath(i ->
                DeleteRecipeRequest.builder()
                        .withRecipeId(i.get("recipeId"))
                        .build());


        return super.runActivity(() -> input.fromUserClaims(claims ->
                DeleteRecipeRequest.builder()
                                .withRecipeId(unauthenticatedRequest.getRecipeId())
                                .withUserId(claims.get("email"))
                                .build()), (request, serviceComponent) ->
                        serviceComponent.provideDeleteRecipeActivity().handleRequest(request)
        );
    }
}
