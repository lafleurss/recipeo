package recipeo.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import recipeo.activity.requests.GetRecipeRequest;
import recipeo.activity.results.GetRecipeResult;

public class GetRecipeLambda extends LambdaActivityRunner<GetRecipeRequest, GetRecipeResult>
implements RequestHandler<LambdaRequest<GetRecipeRequest>, LambdaResponse> {

    /**
     * @param input   The Lambda Function input
     * @param context The Lambda execution environment context object.
     * @return
     */
    @Override
    public LambdaResponse handleRequest(LambdaRequest<GetRecipeRequest> input, Context context) {
        //log.info("handleRequest");
        return null;

//        return super.runActivity(
//                () -> input.fromPath(path ->
//                        GetRecipeRequest.builder()
//                                .withId(path.get("id"))
//                                .build()),
//                (request, serviceComponent) ->
//                        serviceComponent.provideGetPlaylistActivity().handleRequest(request)
//        );
    }
}
