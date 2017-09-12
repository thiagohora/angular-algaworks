package br.com.thiagohora.infrastructure.event.listener;


import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import br.com.thiagohora.infrastructure.event.CreateEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class CreateEventListener implements ApplicationListener<CreateEvent> {

    @Override
    public void onApplicationEvent(CreateEvent event) {
        HttpServletResponse response = event.getResponse();
        Object codigo = event.getCodigo();

        adicionarHeaderLocation(response, codigo);
    }

    private void adicionarHeaderLocation(HttpServletResponse response, Object codigo) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
                .buildAndExpand(codigo).toUri();
        response.setHeader("Location", uri.toASCIIString());
    }

}
