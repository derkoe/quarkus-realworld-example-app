package realworld.config;

import io.quarkus.jsonb.JsonbConfigCustomizer;

import javax.inject.Singleton;

@Singleton
public class JsonbConfig implements JsonbConfigCustomizer {

    public void customize(javax.json.bind.JsonbConfig config) {
        config.withNullValues(true);
    }

}
