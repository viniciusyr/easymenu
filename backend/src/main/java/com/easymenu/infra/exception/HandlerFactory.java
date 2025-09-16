package com.easymenu.infra.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HandlerFactory {

    private URI type;
    private String title;
    private HttpStatus status;
    private String detail;
    private Instant timestamp;
    private Map<String, Object> violations = new LinkedHashMap<>();

    public static HandlerFactoryBuilder builder() {
        return new HandlerFactoryBuilder();
    }

    public static class HandlerFactoryBuilder {
        private final HandlerFactory problem;

        public HandlerFactoryBuilder() {
            this.problem = new HandlerFactory();
        }

        public HandlerFactoryBuilder withType(URI type) {
            problem.type = type;
            return this;
        }

        public HandlerFactoryBuilder withTitle(String title) {
            problem.title = title;
            return this;
        }

        public HandlerFactoryBuilder withStatus(HttpStatus status) {
            problem.status = status;
            return this;
        }

        public HandlerFactoryBuilder withDetail(String detail) {
            problem.detail = detail;
            return this;
        }

        public HandlerFactoryBuilder withTime(Instant time){
            problem.timestamp = time;
            return this;
        }

        public HandlerFactoryBuilder with(String key, Object value) {
            problem.violations.put(key, value);
            return this;
        }

        public HandlerFactoryBuilder with(Map<String, Object> problems){
            problem.violations = problems;
            return this;
        }

        public HandlerFactory build() {
            return problem;
        }
    }
}
