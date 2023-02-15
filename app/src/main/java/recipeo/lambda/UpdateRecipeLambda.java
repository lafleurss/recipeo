package recipeo.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.UpdateRecipeRequest;
import recipeo.activity.results.UpdateRecipeResult;

public class UpdateRecipeLambda extends LambdaActivityRunner<UpdateRecipeRequest, UpdateRecipeResult>
        implements RequestHandler<AuthenticatedLambdaRequest<UpdateRecipeRequest>, LambdaResponse> {
    private final Logger log = LogManager.getLogger();

    /**
     * @param input   The Lambda Function input
     * @param context The Lambda execution environment context object.
     * @return a LambdaResponse
     */
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<UpdateRecipeRequest> input, Context context) {
        log.info("Received UpdateRecipeLambda {}" , input);

        UpdateRecipeRequest unauthenticatedRequest = input.fromBody(UpdateRecipeRequest.class);

        UpdateRecipeRequest unauthenticatedRequestPath = input.fromPath(i ->
                UpdateRecipeRequest.builder()
                        .withRecipeId(i.get("recipeId"))
                        .build());


        return super.runActivity(() -> input.fromUserClaims(claims ->
                UpdateRecipeRequest.builder()
                        .withRecipeId(unauthenticatedRequestPath.getRecipeId())
                        .withRecipeName(unauthenticatedRequest.getRecipeName())
                        .withServings(unauthenticatedRequest.getServings())
                        .withPrepTime(unauthenticatedRequest.getPrepTime())
                        .withCookTime(unauthenticatedRequest.getCookTime())
                        .withTotalTime(unauthenticatedRequest.getTotalTime())
                        .withIngredients(unauthenticatedRequest.getIngredients())
                        .withInstructions(unauthenticatedRequest.getInstructions())
                        .withTags(unauthenticatedRequest.getTags())
                        .withCategoryName(unauthenticatedRequest.getCategoryName())
                        .withIsFavorite(unauthenticatedRequest.getIsFavorite())
                        .withUserId(claims.get("email"))
                        .build()), (request, serviceComponent) ->
                serviceComponent.provideUpdateRecipeActivity().handleRequest(request)
        );
    }
}

