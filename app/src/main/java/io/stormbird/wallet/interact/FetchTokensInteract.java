package io.stormbird.wallet.interact;

import io.stormbird.wallet.entity.*;
import io.stormbird.wallet.repository.TokenRepositoryType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.stormbird.token.entity.MagicLinkData;

public class FetchTokensInteract {

    private final TokenRepositoryType tokenRepository;

    public FetchTokensInteract(TokenRepositoryType tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Observable<TokenInfo> getTokenInfo(String address) {
        return tokenRepository.update(address);
    }

    public Observable<Token[]> fetchStored(Wallet wallet) {
        return tokenRepository.fetchActiveStored(wallet.address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Token> fetchStoredToken(Wallet wallet, String tokenAddress) {
        return tokenRepository.fetchCachedSingleToken(wallet.address, tokenAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Token[]> fetchStoredWithEth(NetworkInfo network, Wallet wallet) {
        return tokenRepository.fetchActiveStoredPlusEth(network, wallet.address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Token> fetchSingle(Wallet wallet, Token token) {
        return tokenRepository.fetchActiveSingle(wallet.address, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updateBalance(Wallet wallet, Token token, List<Integer> burnList) {
        return tokenRepository
                        .setBurnList(wallet, token, burnList)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Token> fetchEth(NetworkInfo network, Wallet wallet)
    {
        return tokenRepository.getEthBalance(network, wallet).toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<BigInteger> getLatestBlock()
    {
        return tokenRepository.fetchLatestBlockNumber();
    }

    public Observable<ContractResult> getContractName(String address, int chainId)
    {
        return tokenRepository.getTokenName(address, chainId).toObservable();
    }

    public Observable<Token> updateDefaultBalance(Token token, NetworkInfo network, Wallet wallet)
    {
        return tokenRepository.fetchActiveTokenBalance(token, network, wallet)
                .subscribeOn(Schedulers.io());
    }

    public Observable<OrderContractAddressPair> updateBalancePair(Token token, MagicLinkData order)
    {
        return tokenRepository.fetchActiveTokenBalance(order.ownerAddress, token)
                .map(updateToken -> mapToPair(updateToken, order))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Token> updateBalance(String address, Token token)
    {
        if (token == null) return Observable.fromCallable(() -> {
            return new Token(null, BigDecimal.ZERO, 0);
        });
        return tokenRepository.fetchActiveTokenBalance(address, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Ticker> getEthereumTicker()
    {
        return tokenRepository.getEthTicker();
    }

    private OrderContractAddressPair mapToPair(Token token, MagicLinkData so)
    {
        OrderContractAddressPair pair = new OrderContractAddressPair();
        pair.order = so;
        pair.balance = token.getArrayBalance();
        return pair;
    }
}
