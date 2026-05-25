# fuelink-blog

`fuelink-blog` 是一个前后端分离的个人博客系统，包含博客前台、后台管理端和 Spring Boot 后端服务。

## 项目结构

```text
.
├── Blogback/        # Spring Boot 后端服务
├── BlogFront/       # Vue 3 后台管理端
├── BlogFront-end/   # Vue 3 博客前台
└── blog.sql         # 数据库初始化脚本
```

## 功能概览

- 博客前台：文章列表、文章详情、归档、友链、关于页、追番页、日报图片展示。
- 后台管理：登录鉴权、文章管理、标签管理、友链管理、句子和内容维护。
- 后端接口：MySQL 数据访问、JWT 鉴权、分页查询、Argon2 密码校验、阿里云 OSS 上传。

## 技术栈

- 后端：Java 17、Spring Boot 3.5、MyBatis、MySQL、PageHelper、JWT、Argon2、Aliyun OSS SDK。
- 博客前台：Vue 3、Vite、Vue Router、Element Plus、Axios、Cherry Markdown、ECharts、Mermaid。
- 后台管理端：Vue 3、Vite、Vue Router、Element Plus、Axios、Cherry Markdown、wangEditor。

## 本地运行

### 1. 准备环境

- JDK 17+
- Node.js 18+
- MySQL 8+

### 2. 初始化数据库

1. 创建 MySQL 数据库。
2. 导入根目录的 `blog.sql`。
3. 如需保留自己的线上数据，导入前先检查 SQL 中的示例内容和图片地址。

### 3. 配置后端

后端配置文件位于 `Blogback/src/main/resources/application.yml`。仓库里的版本已经保留了默认的 MySQL JDBC 地址和 JWT 过期时间，你只需要把占位符改成自己的值。

需要填写的配置：

| 配置项 | 说明 |
| --- | --- |
| `spring.datasource.username` | MySQL 用户名 |
| `spring.datasource.password` | MySQL 密码 |
| `blog.jwt.admin-secret-key` | 后台 JWT 签名密钥 |
| `blog.alioss.access-key-id` | 阿里云 AccessKey ID |
| `blog.alioss.access-key-secret` | 阿里云 AccessKey Secret |
| `blog.alioss.bucket-name` | OSS Bucket 名称 |
| `blog.alioss.public-base-url` | OSS 公网访问基础地址 |

通常不需要改的默认项：

| 配置项 | 默认值 |
| --- | --- |
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/blog?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai` |
| `blog.jwt.admin-ttl` | `7200000` |
| `blog.jwt.admin-token-name` | `token` |
| `blog.alioss.endpoint` | `oss-cn-beijing.aliyuncs.com` |

### 4. 初始化管理员账号

`blog.sql` 只包含建表语句，不包含默认账号。首次启动前，可以在 `application.yml` 中临时开启管理员初始化：

```yaml
blog:
  admin-initializer:
    enabled: true
    id: 1
    username: your_admin_username
    password: your_admin_password
```

启动后程序会自动对密码做 Argon2 加密并写入 `users` 表。如果账号已存在，会跳过创建。初始化成功后，请把 `enabled` 改回 `false`，避免后续误操作。

如果你要提交代码，请确认 `application.yml` 里没有真实密码、JWT 密钥或 AccessKey。

### 5. 配置前端展示信息

博客前台的站点名、作者名、联系方式、社交链接、友链申请信息、备案号、外部接口地址集中在：

```text
BlogFront-end/src/config/siteConfig.js
```

上线前请按需替换其中的占位符，例如：

| 配置项 | 说明 |
| --- | --- |
| `siteTitle` | 页面主标题 |
| `ownerName` | 作者或站点所有者名称 |
| `contact.email` | 友链申请和关于页展示邮箱 |
| `repositoryUrl` | 页面导航里的项目地址 |
| `beian.icpText` | ICP 备案号 |
| `beian.policeText` | 公安备案号 |
| `bangumi.userId` | Bangumi 用户 ID |
| `todayReport.apiUrl` | 今日日报接口地址 |

后台管理端首页的默认头像、每日一句接口和头像上传地址集中在：

```text
BlogFront/src/config/adminConfig.js
```

### 6. 启动后端

```powershell
cd Blogback
.\mvnw.cmd spring-boot:run
```

默认服务地址：`http://localhost:8080`

### 7. 启动博客前台

```powershell
cd BlogFront-end
npm install
npm run dev
```

默认访问地址：`http://localhost:9090`

### 8. 启动后台管理端

```powershell
cd BlogFront
npm install
npm run dev
```

Vite 会输出本地访问地址，接口通过 `/api` 代理到后端服务。

## 常用命令

```powershell
# 后端测试
cd Blogback
.\mvnw.cmd test

# 博客前台构建
cd BlogFront-end
npm run build

# 后台管理端构建
cd BlogFront
npm run build
```

## 后续计划

- 增加本地文件存储模式，将文章图片、头像等上传文件保存到服务器本地目录或挂载磁盘中，减少对 OSS 和 CDN 的依赖。
- 将文件访问地址统一由后端生成，前端只保存资源路径，方便在本地存储、OSS 或其他对象存储之间切换。
- 后台管理端增加上传配置说明和文件管理能力，便于查看、替换和清理已上传资源。
- 保留 OSS 作为可选部署方案，但不作为项目运行的必要依赖。
- 继续完善文章、标签、关于页和友链等内容管理体验，让个人博客更适合长期维护。
