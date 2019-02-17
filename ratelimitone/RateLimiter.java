package ratelimitone;

import java.sql.Time;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class RateLimiter extends InMemoryCache {

    private long capacity;
    private long windowTimeInSeconds;

    long lastRefillTimeStamp;

    long refillCountPerSecond;

    long availableTokens;

    public RateLimiter(long capacity, long windowTimeInSeconds){
        this.capacity = capacity;
        this.windowTimeInSeconds = windowTimeInSeconds;
        lastRefillTimeStamp = System.currentTimeMillis();
        refillCountPerSecond = capacity / windowTimeInSeconds;
        availableTokens = 0;
    }
    public long getAvailableTokens(){
        return this.availableTokens;
    }
    public boolean tryConsume(){
        refill();
        
        if(availableTokens > 0)
        {
            --availableTokens;
            return true;
        }
        else
        {
            return false;
        }
    }
    private void refill(){
        long now = System.currentTimeMillis();
        if(now > lastRefillTimeStamp)
        {
            long elapsedTime = now - lastRefillTimeStamp;
            //refill tokens for this duration
            long tokensToBeAdded = (elapsedTime/1000) * refillCountPerSecond;
            if(tokensToBeAdded > 0) {
                availableTokens = Math.min(capacity, availableTokens + tokensToBeAdded);
                lastRefillTimeStamp = now;
               
            }
        }
    }
    public static void main(String[] args){
        RateLimiter rl=new RateLimiter(525478545,13493);
        
        InMemoryCache imc=new InMemoryCache();
        String key="C1";
        Object value=null;
        long periodInMillis=515154512;
        imc.add(key, value, periodInMillis);
        System.out.println(System.currentTimeMillis());//rl.getAvailableTokens());
        System.out.println(rl.tryConsume());
    }
}
