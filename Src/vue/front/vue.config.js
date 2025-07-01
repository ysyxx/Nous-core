// var webpack = require('webpack');
//vue2
const path = require('path')
function resolve(dir) {
    return path.join(__dirname, dir)
}
function publicPath(){
    if (process.env.NODE_ENV == 'production') {
        return "././";
    } else {
        return "/";
    }
}
// vue.config.js
module.exports = {
    // publicPath:"././",
    publicPath: publicPath(),
    // 国际化配置 使用其它语言，默认情况下中文语言包依旧是被引入的
    configureWebpack: {
        // plugins: [
        //     new webpack.NormalModuleReplacementPlugin(/element-ui[\/\\]lib[\/\\]locale[\/\\]lang[\/\\]zh-CN/, 'element-ui/lib/locale/lang/en')
        // ]
        resolve: {
            alias: {
                '@': resolve('src')
            }
        }
    },
lintOnSave: false,
    devServer: {
        host: "0.0.0.0", //指定使用一个 host。默认是 localhost，这里默认值即可
        port: 8082, //指定端口
        hot: true, // 开启热更新
        https: false, // 是否开启https模式
        // proxy: {
        // // 后端服务，通过网关访问
        // '/springbootn3op2l20': {
        //     target: 'http://localhost:9000', // 代理到网关服务
        //     changeOrigin: true,
        //     secure: false,
        //     },

        // // AI 服务，通过网关访问
        // '/api/ai': {
        //     target: 'http://localhost:9000', // 同样代理到网关服务
        //     changeOrigin: true,
        //     secure: false,
        //     }
        // },
    },
chainWebpack(config) {
    config.module
      .rule('svg')
      .exclude.add(resolve('src/icons'))
      .end()
    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('src/icons'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]'
      })
      .end()
}
}
