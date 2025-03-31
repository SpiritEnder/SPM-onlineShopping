//product.js

import myAxios from "@/config/axios";

//搜索商品
export function searchProduct(keyword, page = 1, limit = 20) {
    return myAxios({
        url: '/api/product',
        method: 'get',
        params: {
            'keyword': keyword,
            'page': page,
            'limit': limit
        }
    })
}

//精确查找
export function productInfo(id) {
    return myAxios({
        url: '/api/product/search/' + id,
        method: 'get',
        params: {
        }
    })
}


//注册
export function productRegister(username, password, email, phoneNumber) {
    return myAxios({
        url: '/api/auth/register',
        method: 'post',
        headers: {
            "Content-Type": 'application/json'
        },
        data: {
            'username': username,
            'password': password,
            'email': email,
            'phone': phoneNumber
        }
    })
}