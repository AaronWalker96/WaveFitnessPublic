package com.wave.fitness;

import java.util.EnumMap;
import java.util.List;

/**
 * Created by s6236422 on 14/05/2017.
 */

public class SpotifyPlaylists {

    /* Class containing all the music Wave Plays */

    public enum Genre{
        POP, CLASSICAL, ELECTRONIC, FUNK, ROCK, JAZZFUSION, INDIEROCK, HITS
    }

    public static EnumMap<Genre, String[]> allGenre = new EnumMap<Genre, String[]>(Genre.class);

    public static final String[] popGenre = {
            "spotify:user:spotify:playlist:37i9dQZF1DWY4lFlS4Pnso",
            "spotify:user:spotify:playlist:37i9dQZF1DWSVtp02hITpN",
            "spotify:user:spotify:playlist:37i9dQZF1DXdc6Ams1C6tL",
            "spotify:user:spotify:playlist:37i9dQZF1DX8vwRmUsEIMT",
            "spotify:user:spotify:playlist:37i9dQZF1DWVxqA801UP5W",
            "spotify:user:spotify:playlist:37i9dQZF1DWVrBRunTOXCY",
            "spotify:user:spotify:playlist:37i9dQZF1DWX3xqQKu0Sgn",
            "spotify:user:spotify:playlist:37i9dQZF1DX1IeqVkK7Ebc",
            "spotify:user:spotify:playlist:37i9dQZF1DWYp3yzk1civi",
            "spotify:user:spotify:playlist:37i9dQZF1DWYp3yzk1civi",
            "spotify:user:spotify:playlist:37i9dQZF1DX4v0Y84QklHD",
            "spotify:user:spotify:playlist:37i9dQZF1DWUa8ZRTfalHk",
            "spotify:user:spotify:playlist:37i9dQZF1DXcBWIGoYBM5M",
            "spotify:user:spotify:playlist:7hshURH8dzz84ubAwlDJVX",
            "spotify:user:spotify:playlist:1QM1qz09ZzsAPiXphF1l4S",
    };

    public static final String[] classicalGenre = {
            "spotify:user:spotify:playlist:7MizIujRqHWLFVZAfQ21h4",
            "spotify:user:spotify:playlist:37i9dQZF1DX561TxkFttR4",
            "spotify:user:spotify:playlist:37i9dQZF1DXah8e1pvF5oE",
            "spotify:user:spotify:playlist:37i9dQZF1DX9l01QzlK1Yo",
            "spotify:user:spotify:playlist:37i9dQZF1DWWEJlAGA9gs0",
            "spotify:user:spotify:playlist:37i9dQZF1DWV0gynK7G6pD",
            "spotify:user:spotify:playlist:37i9dQZF1DX6Oqe1LC2A2s",
            "spotify:user:spotify:playlist:4vGoXR0GZikU6TjGyMeIOa",
            "spotify:user:spotify:playlist:37i9dQZF1DX4s3V2rTswzO",
    };

    public static final String[] electronicGenre = {
            "spotify:user:spotify:playlist:37i9dQZF1DX5uokaTN4FTR",
            "spotify:user:spotify:playlist:37i9dQZF1DWSqPHam7LOqC",
            "spotify:user:spotify:playlist:37i9dQZF1DWSrVdvTl1tVY",
            "spotify:user:spotify:playlist:37i9dQZF1DX4dyzvuaRJ0n",
            "spotify:user:spotify:playlist:37i9dQZF1DXcZDD7cfEKhW",
            "spotify:user:spotify:playlist:37i9dQZF1DX0BcQWzuB7ZO",
            "spotify:user:spotify:playlist:37i9dQZF1DWVxqA801UP5W",
            "spotify:user:spotify:playlist:2dNg1M1dg4DZlSNBZqtXCQ",
            "spotify:user:spotify:playlist:5F11vjJWpNuDxKvUj9VyoE",
    };

    public static final String[] funkGenre = {
            "spotify:user:spotify:playlist:37i9dQZF1DX23YPJntYMnh",
            "spotify:user:spotify:playlist:37i9dQZF1DX6drTZKzZwSo",
            "spotify:user:spotify:playlist:37i9dQZF1DWSrVdvTl1tVY",
            "spotify:user:spotify:playlist:37i9dQZF1DX48ZHftL4a1N",
            "spotify:user:spotify:playlist:0yeWhMQqKLIy50RPAUINTV",
            "spotify:user:spotify:playlist:4Ebgfxsss10NoZbpRq1E06",
            "spotify:user:spotify:playlist:37i9dQZF1DWZgauS5j6pMv",
            "spotify:user:spotify:playlist:37i9dQZF1DX0N8QTiMHLoT",
            "spotify:user:spotify:playlist:37i9dQZF1DX8f5qTGj8FYl",

    };

    public static final String[] rockGenre = {
            "spotify:user:spotify:playlist:37i9dQZF1DX4DZAVUAwHMT",
            "spotify:user:spotify:playlist:37i9dQZF1DWYFysGpUi8gb",
            "spotify:user:spotify:playlist:37i9dQZF1DX1tyCD9QhIWF",
            "spotify:user:spotify:playlist:37i9dQZF1DX0rCrO4CFRfM",
            "spotify:user:spotify:playlist:37i9dQZF1DWXRqgorJj26U",
            "spotify:user:spotify:playlist:37i9dQZF1DXcF6B6QPhFDv",
            "spotify:user:spotify:playlist:37i9dQZF1DX49jUV2NfGku",
            "spotify:user:spotify:playlist:37i9dQZF1DWZn9s1LNKPiM",
            "spotify:user:spotify:playlist:37i9dQZF1DX153gOfbCM2i",
            "spotify:user:spotify:playlist:37i9dQZF1DWT2jS7NwYPVI"
    };

    public static final String[] jazzfusionGenre = {
            "spotify:user:metal_swe:playlist:3aCjMq3zibs1US60n0KRpA",
            "spotify:user:rainbowdash2:playlist:1yBg3b59yAnbADsjPAeczZ",
            "spotify:user:maxplank1:playlist:4wSYdFIuOiLuICzcBiHDKv",
            "spotify:user:shmokiman:playlist:4CTbClgGZJQNBdn0xhfVmt",
            "spotify:user:grand3puba:playlist:7aOaM9Ri21TtUu0CiJ7OOb",
    };

    public static final String[] indierockGenre = {
            "spotify:user:spotify:playlist:37i9dQZF1DWVTKDs2aOkxu",
            "spotify:user:spotify:playlist:37i9dQZF1DWYBF1dYDPlHw",
            "spotify:user:spotify:playlist:37i9dQZF1DXazOBCyyv15E",
            "spotify:user:spotify:playlist:37i9dQZF1DX81ZHwIgVFcs",
            "spotify:user:spotify:playlist:37i9dQZF1DX0CIO5EOSHeD",
            "spotify:user:spotify:playlist:37i9dQZF1DX6uhsAfngvaD",
            "spotify:user:spotify:playlist:37i9dQZF1DWSV5Sri2Qa60",
            "spotify:user:spotify:playlist:37i9dQZF1DX0YKekzl0blG",
            "spotify:user:spotify:playlist:37i9dQZF1DXaLI8a395lse",
            "spotify:user:spotify:playlist:37i9dQZF1DWTSKFpOdYF1r"
    };

    public static final String[] hitsGenre = {
            "spotify:user:spotify:playlist:37i9dQZF1DXb57FjYWz00c",
            "spotify:user:spotify:playlist:37i9dQZF1DWV8xrpik0esU",
            "spotify:user:spotify:playlist:37i9dQZF1DX8j4KHUVrE2f",
            "spotify:user:spotify:playlist:37i9dQZF1DWXF8Nf1uycDZ",
            "spotify:user:spotify:playlist:37i9dQZF1DX6FdIcKzUp2r",
            "spotify:user:spotify:playlist:37i9dQZF1DWVYv8jwZyr6h",
            "spotify:user:spotify:playlist:37i9dQZF1DX6l1fwN15uV5",
            "spotify:user:spotify:playlist:37i9dQZF1DWSWNiyXQAvbl",
            "spotify:user:spotify:playlist:37i9dQZF1DXdj82GcM2wq2",
            "spotify:user:spotify:playlist:37i9dQZF1DX4o1oenSJRJd"
    };

}
