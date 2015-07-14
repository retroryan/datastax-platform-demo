package movieAnalysis;

import java.io.Serializable;

public class RatingData implements Serializable {

    int movie_id;
    int user_id;
    float rating;
    int timestamp;

    public RatingData(int movie_id, int user_id, float rating, int timestamp) {
        this.movie_id = movie_id;
        this.user_id = user_id;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "RaitingData{" +
                "movie_id=" + movie_id +
                ", user_id=" + user_id +
                ", rating=" + rating +
                ", timestamp=" + timestamp +
                '}';
    }
}
