import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class TestApi extends Data {

    @BeforeClass
    public static void urlBase() {
        RestAssured.baseURI = "https://api.thecatapi.com/v1/";
    }

    @Test
    public void registry() {
        Response response =
                    given().
                        contentType("application/json").
                        body(bodyRegistry).
                    when().
                        post(urlRegistry);

        validation(response);
    }

    @Test
    public void vote() {
        Response response =
                    given().
                        contentType("application/json").
                        body(bodyVote).
                    when().
                        post("votes/");

        validation(response);

        String id = response.jsonPath().getString("id");
        System.out.println("ID => " + id);
        voteId = id;
    }

    @Test
    public void removeVote() {
        vote();
        removeVoteById();
    }

    public void removeVoteById() {
        Response response =
                    given().
                        contentType("application/json").
                        header("x-api-key", "2098c68c-f11b-4507-936e-cf68e8d7f37c").
                        pathParam("vote_id", voteId).
                    when().
                        delete("votes/{vote_id}");

        validation(response);
    }

    @Test
    public void addFavRemoveFav() {
        addFav();
        removeFav();
    }

    public void addFav() {
        Response response =
                    given().
                        contentType("application/json").
                        header("x-api-key", "2098c68c-f11b-4507-936e-cf68e8d7f37c").
                        body(bodyAddFav).
                    when().
                        post("favourites");
        
        String id = response.jsonPath().getString("id");
        favId = id;

        validation(response);
    }

    public void removeFav() {
        Response response =
                given().
                    contentType("application/json").
                    header("x-api-key", "2098c68c-f11b-4507-936e-cf68e8d7f37c").
                    pathParam("favourite_id", favId).
                when().
                    delete("favourites/{favourite_id}");

        validation(response);
    }

    public void validation(Response response) {
        response.then().body("message", containsString("SUCCESS")).statusCode(200);
        System.out.println("RESPONSE => " + response.body().asString());
        System.out.println("------------");
    }
}
