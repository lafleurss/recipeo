package recipeo.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.CreateRecipeRequest;
import recipeo.activity.results.CreateRecipeResult;

public class CreateRecipeLambda extends LambdaActivityRunner<CreateRecipeRequest, CreateRecipeResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateRecipeRequest>, LambdaResponse> {
    private final Logger log = LogManager.getLogger();

    /**
     * @param input   The Lambda Function input
     * @param context The Lambda execution environment context object.
     * @return a LambdaResponse
     */
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateRecipeRequest> input, Context context) {
        log.info("Received CreateRecipeLambda {}" , input);

        CreateRecipeRequest unauthenticatedRequest = input.fromBody(CreateRecipeRequest.class);

        return super.runActivity(() -> input.fromUserClaims(claims ->
                CreateRecipeRequest.builder()
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
                serviceComponent.provideCreateRecipeActivity().handleRequest(request)
        );
    }
}

