const base = {
    get() {
        return {
            url : "http://localhost:8080/springbootn3op2l20/",
            name: "springbootn3op2l20",
            // 退出到首页链接
            indexUrl: 'http://localhost:8080/springbootn3op2l20/front/dist/index.html'
        };
    },
    getProjectName(){
        return {
            projectName: "启熹LearnLume"
        } 
    }
}
export default base
