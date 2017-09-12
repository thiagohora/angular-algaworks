package br.com.thiagohora.infrastructure.event;

import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletResponse;

public class CreateEvent<ID> extends ApplicationEvent {

    private HttpServletResponse response;
    private ID codigo;

    public CreateEvent(final Object source, final ID codigo, final HttpServletResponse response) {
        super(source);
        this.codigo = codigo;
        this.response = response;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public ID getCodigo() {
        return codigo;
    }

    public static CreateEventBuilder newBuilder(Object source) {
        return new CreateEventBuilder(source);
    }

    private static class CreateEventBuilder<ID> {
        private  HttpServletResponse response;
        private ID codigo;
        private Object source;

        private CreateEventBuilder(Object source) {
            this.source = source;
        }

        public CreateEventBuilder<ID> setResponse(HttpServletResponse response) {
            this.response = response;
            return this;
        }

        public CreateEventBuilder<ID> setCodigo(ID codigo) {
            this.codigo = codigo;
            return this;
        }

        public CreateEvent<ID> build() {
            return new CreateEvent<>(source, codigo, response);
        }
    }
}
