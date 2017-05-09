package com.pokemonworld.webservice;

/**
 * Created by J. Paulo on 05/05/2017.
 */

public class RedeNaoDisponivelException extends Exception {

    public RedeNaoDisponivelException() {
    }

    public RedeNaoDisponivelException(String message) {
        super(message);
    }

    public RedeNaoDisponivelException(Throwable cause) {
        super(cause);
    }

    public RedeNaoDisponivelException(String message, Throwable cause) {
        super(message, cause);
    }

}
