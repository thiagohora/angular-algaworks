package br.com.thiagohora.infrastructure.security.token.customize;

import br.com.thiagohora.infrastructure.security.token.user.UserInfo;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(final OAuth2AccessToken token, final OAuth2Authentication authentication) {

        final UserInfo user = (UserInfo) authentication.getPrincipal();

        final DefaultOAuth2AccessToken accessToken = (DefaultOAuth2AccessToken) token;

        accessToken.setAdditionalInformation(Stream.of(new SimpleEntry<>("name", user.getName()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        return accessToken;
    }
}
