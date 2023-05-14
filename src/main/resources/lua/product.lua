local productCode=KEYS[1]
local buyQty = tonumber(ARGV[1])

local productExist = redis.call('exists',productCode)
redis.log(redis.LOG_DEBUG, productExist)

if productExist == 1 then
    local curStock = tonumber(redis.call('get',productCode))
    redis.log(redis.LOG_DEBUG, curStock)
    if curStock > 0 and curStock-buyQty >= 0 then
        redis.call('decrby',productCode,buyQty)
        return 0;
    else
        return 1;
    end
else
    return 2;
end
