package ru.alfabank.testtask.services.gifsource;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="gifSource", url="${services.gif_source.url}")
public interface GifServiceProxy {

    @GetMapping(value=  "/v1/gifs/random" +
                        "?api_key=${services.gif_source.api_key}" +
                        "&tag={tag}" +
                        "&rating=g")
    public String getJsonWithRandomGifByTag(@PathVariable("tag") String tag)
        throws Exception;

}
