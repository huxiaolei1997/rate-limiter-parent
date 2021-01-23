--
-- Created by IntelliJ IDEA.
-- User: huxiaolei
-- Date: 2021-01-23
-- Time: 15:44
-- To change this template use File | Settings | File Templates.
--

-- 方法签名特征
local methodKey = KEYS[1]
redis.log(redis.LOG_DEBUG, 'key is ', methodKey)

-- 调用脚本传入的限流大小
local limit = tonumber(ARGV[1])


local count = tonumber(redis.call('get', methodKey) or "0")

-- 是否超出限流阈值
if count + 1 > limit then
    return false
else
    redis.call('incrby', methodKey, 1)
    redis.call('expire', methodKey, 1)
    return true
end



