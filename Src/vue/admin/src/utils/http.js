import axios from 'axios'
import router from '@/router/router-static'
import { Message } from 'element-ui'
import storage from "@/utils/storage"; // 引入我们封装的storage

const http = axios.create({
    baseURL: '/springbootn3op2l20'
})

// request拦截器
http.interceptors.request.use(
    config => {
        // 【核心】在发送请求前，从storage中获取Token
        const token = storage.get('Token'); // 确保键名'Token'大小写正确
        if (token) {
            // 如果Token存在，则将其放入请求头中
            config.headers.token = token;
        }
        return config
    },
    error => {
        return Promise.reject(error)
    }
)

// response拦截器
http.interceptors.response.use(
    response => {
        // 如果后端返回401，说明token无效或过期，跳转到登录页
        if (response.data.code == 401) {
            storage.clear() // 清空所有本地存储
            router.push({
                name: 'login'
            })
            Message.error("登录凭证已过期，请重新登录")
        }
        return response
    },
    error => {
        Message.error(error.message)
        return Promise.reject(error)
    }
)
export default http