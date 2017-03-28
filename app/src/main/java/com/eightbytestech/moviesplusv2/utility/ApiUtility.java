package com.eightbytestech.moviesplusv2.utility;


import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtility {

    public static final String TAG = "#ApiUtility: ";


    private static String movieDbEndPoint;
    private static String movieApiKey;
    private static String movieLanguage;
    private static String moviePhotoBaseUrl;
    private static String moviePhotoSizeUrl;


    public static void setMovieDbApiValues(String endPoint, String apiKey, String language, String photoBaseUrl, String photoSizeUrl) {
        movieDbEndPoint = endPoint;
        movieApiKey = apiKey;
        movieLanguage = language;
        moviePhotoBaseUrl = photoBaseUrl;
        moviePhotoSizeUrl = photoSizeUrl;
    }

    public static class MovieDbUtility {

        private static final String API_BASE_URL = movieDbEndPoint;
        private static final String API_KEY = movieApiKey;
        private static final String MOVIE_LANGUAGE = movieLanguage;

        private static final String APPEND_TO_RESPONSE_KEY = "append_to_response";
        private static final String REVIEWS_VIDEOS_PARAM = "reviews,videos";
        private static final String APPEND_TO_RESPONSE_QUERY = APPEND_TO_RESPONSE_KEY + "=" + REVIEWS_VIDEOS_PARAM;

        private static final String PHOTOS_BASE_URL = moviePhotoBaseUrl;
        private static final String PHOTOS_SIZE_URL = moviePhotoSizeUrl;

        public static MovieDBEndpointInterface getMovieDbEndpointInterface() {
            Gson gson = new Gson();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            return retrofit.create(MovieDBEndpointInterface.class);
        }

        public static String getCompletePhotoUrl(String photoUrl) {
            String completeUrl = PHOTOS_BASE_URL +
                    PHOTOS_SIZE_URL +
                    photoUrl;
            return completeUrl;
        }
    }

    public static class YoutubeUtility {

        private static final String YOUTUBE_IMAGE_PREFIX = "http://img.youtube.com/vi/";
        private static final String YOUTUBE_IMAGE_POSTFIX = "/0.jpg";
        private static final String YOUTUBE_TRAILER_PREFIX = "https://www.youtube.com/watch?v=";

        /*given a youtube trailer id, generate the thumbnail url */
        public static String getPosterUrlFromTrailerId(String trailerIdString) {
            return YOUTUBE_IMAGE_PREFIX + trailerIdString + YOUTUBE_IMAGE_POSTFIX;
        }

        /*given a youtube trailer id, generate the trailer url */
        public static String getTrailerUrlFromTrailerId(String trailerId) {
            return YOUTUBE_TRAILER_PREFIX + trailerId;
        }
    }
}
