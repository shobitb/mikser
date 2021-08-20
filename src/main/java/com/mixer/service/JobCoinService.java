package com.mixer.service;

import com.mixer.models.Address;
import com.mixer.models.PostTransaction;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.math.BigDecimal;

/**
 * API interface and implementation of JobCoinService
 */
public class JobCoinService {
    private static final String BASE_URI = "http://jobcoin.gemini.com/pagan-unsold/api";

    private final JobCoinServiceClient client;

    public JobCoinService() {
        final ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
        this.client = resteasyClient.target(BASE_URI).proxy(JobCoinServiceClient.class);
    }

    public void postTransaction(final String fromAddress, final String toAddress,
                                final BigDecimal amount) throws JobCoinServiceException {
        try {
            client.postTransaction(new PostTransaction(fromAddress, toAddress, amount));
        } catch (final BadRequestException ex) {
            throw new JobCoinServiceException(
                    String.format("Failure when transferring from %s to %s", fromAddress, toAddress), ex);
        }
    }

    public BigDecimal getBalance(final String address) {
        return client.getAddress(address).getBalance();
    }

    interface JobCoinServiceClient {
        @GET
        @Path("/addresses/{address}")
        @Produces("application/json")
        public Address getAddress(@PathParam("address") final String address);

        @POST
        @Path("/transactions")
        @Consumes("application/json")
        @Produces("application/json")
        public void postTransaction(final PostTransaction postTxn);
    }
}
