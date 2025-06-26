/*
 Navicat Premium Data Transfer

 Source Server         : root
 Source Server Type    : MySQL
 Source Server Version : 80037 (8.0.37)
 Source Host           : localhost:3306
 Source Schema         : springbootn3op2l20

 Target Server Type    : MySQL
 Target Server Version : 80037 (8.0.37)
 File Encoding         : 65001

 Date: 26/06/2025 08:55:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for chapters
-- ----------------------------
DROP TABLE IF EXISTS `chapters`;
CREATE TABLE `chapters`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `course_id` bigint NOT NULL COMMENT '所属课程ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '章节标题',
  `chapter_order` int NOT NULL DEFAULT 0 COMMENT '章节显示顺序',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `course_id`(`course_id` ASC) USING BTREE,
  CONSTRAINT `chapters_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程章节表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chapters
-- ----------------------------
INSERT INTO `chapters` VALUES (1, 1, '第一章：网络协议概论', 1, '2025-06-25 15:57:17');
INSERT INTO `chapters` VALUES (2, 1, '第二章：物理层', 2, '2025-06-25 15:57:17');

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '配置参数名称',
  `value` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '配置参数值',
  `url` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'url',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '配置文件' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config
-- ----------------------------
INSERT INTO `config` VALUES (1, 'picture1', 'upload/picture1.jpg', NULL);
INSERT INTO `config` VALUES (2, 'picture2', 'upload/picture2.jpg', NULL);
INSERT INTO `config` VALUES (3, 'picture3', 'upload/picture3.jpg', NULL);

-- ----------------------------
-- Table structure for courses
-- ----------------------------
DROP TABLE IF EXISTS `courses`;
CREATE TABLE `courses`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `title` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '课程名称',
  `description` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '课程详细介绍',
  `kechengleixing` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '课程类型',
  `kechengpingfen` int NULL DEFAULT NULL COMMENT '课程评分',
  `cover_image_url` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '课程图片',
  `thumbsupnum` int NULL DEFAULT 0 COMMENT '赞',
  `crazilynum` int NULL DEFAULT 0 COMMENT '踩',
  `clicktime` datetime NULL DEFAULT NULL COMMENT '最近点击时间',
  `clicknum` int NULL DEFAULT 0 COMMENT '点击次数',
  `storeupnum` int NULL DEFAULT 0 COMMENT '收藏数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '课程信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of courses
-- ----------------------------
INSERT INTO `courses` VALUES (1, '2025-06-25 15:57:17', '深入浅出计算机网络', '一门全面的计算机网络入门课程', '计算机科学', NULL, 'http://example.com/cover.jpg', 0, 0, NULL, 0, 0);
INSERT INTO `courses` VALUES (21, '2024-03-05 18:16:45', '动画鉴赏', NULL, '课程类型1', 1, 'upload/kechengxinxi_kechengtupian1.jpg,upload/kechengxinxi_kechengtupian2.jpg,upload/kechengxinxi_kechengtupian3.jpg', 2, 1, '2024-03-05 18:16:45', 82, 3);
INSERT INTO `courses` VALUES (22, '2024-03-05 18:16:45', '课程名称2', NULL, '课程类型2', 2, 'upload/kechengxinxi_kechengtupian2.jpg,upload/kechengxinxi_kechengtupian3.jpg,upload/kechengxinxi_kechengtupian4.jpg', 2, 2, '2024-03-05 18:16:45', 21, 3);
INSERT INTO `courses` VALUES (23, '2024-03-05 18:16:45', '课程名称3', NULL, '课程类型3', 3, 'upload/kechengxinxi_kechengtupian3.jpg,upload/kechengxinxi_kechengtupian4.jpg,upload/kechengxinxi_kechengtupian5.jpg', 3, 3, '2024-03-05 18:16:45', 59, 3);
INSERT INTO `courses` VALUES (24, '2024-03-05 18:16:45', '课程名称4', NULL, '课程类型4', 4, 'upload/kechengxinxi_kechengtupian4.jpg,upload/kechengxinxi_kechengtupian5.jpg,upload/kechengxinxi_kechengtupian6.jpg', 4, 4, '2024-03-05 18:16:45', 7, 4);
INSERT INTO `courses` VALUES (25, '2024-03-05 18:16:45', '课程名称5', NULL, '课程类型5', 5, 'upload/kechengxinxi_kechengtupian5.jpg,upload/kechengxinxi_kechengtupian6.jpg,upload/kechengxinxi_kechengtupian7.jpg', 5, 5, '2024-03-05 18:16:45', 11, 5);
INSERT INTO `courses` VALUES (26, '2024-03-05 18:16:45', '课程名称6', NULL, '课程类型6', 6, 'upload/kechengxinxi_kechengtupian6.jpg,upload/kechengxinxi_kechengtupian7.jpg,upload/kechengxinxi_kechengtupian8.jpg', 6, 6, '2024-03-05 18:16:45', 6, 6);
INSERT INTO `courses` VALUES (27, '2024-03-05 18:16:45', '课程名称7', NULL, '课程类型7', 7, 'upload/kechengxinxi_kechengtupian7.jpg,upload/kechengxinxi_kechengtupian8.jpg,upload/kechengxinxi_kechengtupian9.jpg', 7, 7, '2024-03-05 18:16:45', 7, 7);
INSERT INTO `courses` VALUES (28, '2024-03-05 18:16:45', '课程名称8', NULL, '课程类型8', 8, 'upload/kechengxinxi_kechengtupian8.jpg,upload/kechengxinxi_kechengtupian9.jpg,upload/kechengxinxi_kechengtupian10.jpg', 8, 8, '2024-03-05 18:16:45', 14, 8);
INSERT INTO `courses` VALUES (29, '2024-03-05 18:25:12', 'XX课程', NULL, '数学课程', 9, 'upload/1709634300048.jpg', 0, 0, NULL, 2, 0);
INSERT INTO `courses` VALUES (30, '2025-06-24 15:21:57', '王道操作系统', NULL, '计算机科学', 10, 'https://smart-core.obs.cn-north-4.myhuaweicloud.com/images/test.jpg', 0, 0, '2025-06-24 15:22:45', 46, 0);

-- ----------------------------
-- Table structure for coursevideos
-- ----------------------------
DROP TABLE IF EXISTS `coursevideos`;
CREATE TABLE `coursevideos`  (
  `course_session` int NULL DEFAULT NULL,
  `course_id` int NULL DEFAULT NULL,
  `video_url` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of coursevideos
-- ----------------------------
INSERT INTO `coursevideos` VALUES (1, 30, 'https://smart-core.obs.cn-north-4.myhuaweicloud.com/videos/02.%5B2%5D--1.1_%E8%AE%A1%E7%AE%97%E6%9C%BA%E5%8F%91%E5%B1%95%E5%8E%86%E7%A8%8B.mp4', 1);
INSERT INTO `coursevideos` VALUES (1, 21, 'https://smart-core.obs.cn-north-4.myhuaweicloud.com/videos/0ab2b918-d540-40a6-83e3-bd3abef6a0e7-[06]--1.1_3_2 三种交换方式的性能分析.mp4', 2);
INSERT INTO `coursevideos` VALUES (1, 23, 'https://smart-core.obs.cn-north-4.myhuaweicloud.com/videos/1c7cec82-a20c-4d9a-b1b8-771e3d56ff19-02.[2]--1.1_计算机发展历程.mp4', 3);
INSERT INTO `coursevideos` VALUES (2, 23, 'https://nous-core.obs.cn-north-4.myhuaweicloud.com/videos/%5B03%5D--1.1_1%20%E8%AE%A1%E7%AE%97%E6%9C%BA%E7%BD%91%E7%BB%9C%E7%9A%84%E6%A6%82%E5%BF%B5.mp4', 4);
INSERT INTO `coursevideos` VALUES (3, 23, 'https://nous-core.obs.cn-north-4.myhuaweicloud.com/videos/%5B04%5D--1.1_2%20%E8%AE%A1%E7%AE%97%E6%9C%BA%E7%BD%91%E7%BB%9C%E7%9A%84%E7%BB%84%E6%88%90%E5%92%8C%E5%8A%9F%E8%83%BD.mp4', 5);
INSERT INTO `coursevideos` VALUES (4, 23, 'https://nous-core.obs.cn-north-4.myhuaweicloud.com/videos/%5B06%5D--1.1_3_2%20%E4%B8%89%E7%A7%8D%E4%BA%A4%E6%8D%A2%E6%96%B9%E5%BC%8F%E7%9A%84%E6%80%A7%E8%83%BD%E5%88%86%E6%9E%90.mp4', 6);

-- ----------------------------
-- Table structure for forum
-- ----------------------------
DROP TABLE IF EXISTS `forum`;
CREATE TABLE `forum`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `title` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '帖子标题',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '帖子内容',
  `parentid` bigint NULL DEFAULT NULL COMMENT '父节点id',
  `userid` bigint NOT NULL COMMENT '用户id',
  `username` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `avatarurl` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '头像',
  `isdone` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '状态',
  `istop` int NULL DEFAULT 0 COMMENT '是否置顶',
  `toptime` datetime NULL DEFAULT NULL COMMENT '置顶时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 71 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '互动交流' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of forum
-- ----------------------------
INSERT INTO `forum` VALUES (61, '2024-03-05 18:16:46', '帖子标题1', '帖子内容1', 0, 1, '用户名1', 'upload/forum_avatarurl1.jpg,upload/forum_avatarurl2.jpg,upload/forum_avatarurl3.jpg', '开放', 0, '2024-03-05 18:16:46');
INSERT INTO `forum` VALUES (62, '2024-03-05 18:16:46', '帖子标题2', '帖子内容2', 0, 2, '用户名2', 'upload/forum_avatarurl2.jpg,upload/forum_avatarurl3.jpg,upload/forum_avatarurl4.jpg', '开放', 0, '2024-03-05 18:16:46');
INSERT INTO `forum` VALUES (63, '2024-03-05 18:16:46', '帖子标题3', '帖子内容3', 0, 3, '用户名3', 'upload/forum_avatarurl3.jpg,upload/forum_avatarurl4.jpg,upload/forum_avatarurl5.jpg', '开放', 0, '2024-03-05 18:16:46');
INSERT INTO `forum` VALUES (64, '2024-03-05 18:16:46', '帖子标题4', '帖子内容4', 0, 4, '用户名4', 'upload/forum_avatarurl4.jpg,upload/forum_avatarurl5.jpg,upload/forum_avatarurl6.jpg', '开放', 0, '2024-03-05 18:16:46');
INSERT INTO `forum` VALUES (65, '2024-03-05 18:16:46', '帖子标题5', '帖子内容5', 0, 5, '用户名5', 'upload/forum_avatarurl5.jpg,upload/forum_avatarurl6.jpg,upload/forum_avatarurl7.jpg', '开放', 0, '2024-03-05 18:16:46');
INSERT INTO `forum` VALUES (66, '2024-03-05 18:16:46', '帖子标题6', '帖子内容6', 0, 6, '用户名6', 'upload/forum_avatarurl6.jpg,upload/forum_avatarurl7.jpg,upload/forum_avatarurl8.jpg', '开放', 0, '2024-03-05 18:16:46');
INSERT INTO `forum` VALUES (67, '2024-03-05 18:16:46', '帖子标题7', '帖子内容7', 0, 7, '用户名7', 'upload/forum_avatarurl7.jpg,upload/forum_avatarurl8.jpg,upload/forum_avatarurl9.jpg', '开放', 0, '2024-03-05 18:16:46');
INSERT INTO `forum` VALUES (68, '2024-03-05 18:16:46', '帖子标题8', '帖子内容8', 0, 8, '用户名8', 'upload/forum_avatarurl8.jpg,upload/forum_avatarurl9.jpg,upload/forum_avatarurl10.jpg', '开放', 0, '2024-03-05 18:16:46');
INSERT INTO `forum` VALUES (69, '2024-03-05 18:23:16', '发布帖子', '<p>发布帖子发布帖子发布帖子发布帖子发布帖子发布帖子发布帖子发布帖子</p>', 0, 12, '用户账号2', NULL, '开放', 1, '2025-06-25 16:13:09');

-- ----------------------------
-- Table structure for kechengleixing
-- ----------------------------
DROP TABLE IF EXISTS `kechengleixing`;
CREATE TABLE `kechengleixing`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `kechengleixing` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '课程类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '课程类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of kechengleixing
-- ----------------------------
INSERT INTO `kechengleixing` VALUES (31, '2024-03-05 18:16:45', '课程类型1');
INSERT INTO `kechengleixing` VALUES (32, '2024-03-05 18:16:45', '课程类型2');
INSERT INTO `kechengleixing` VALUES (33, '2024-03-05 18:16:45', '课程类型3');
INSERT INTO `kechengleixing` VALUES (34, '2024-03-05 18:16:45', '课程类型4');
INSERT INTO `kechengleixing` VALUES (35, '2024-03-05 18:16:45', '课程类型5');
INSERT INTO `kechengleixing` VALUES (36, '2024-03-05 18:16:45', '课程类型6');
INSERT INTO `kechengleixing` VALUES (37, '2024-03-05 18:16:45', '课程类型7');
INSERT INTO `kechengleixing` VALUES (38, '2024-03-05 18:16:45', '课程类型8');
INSERT INTO `kechengleixing` VALUES (39, '2024-03-05 18:24:31', '数学课程');

-- ----------------------------
-- Table structure for kechengpingjia
-- ----------------------------
DROP TABLE IF EXISTS `kechengpingjia`;
CREATE TABLE `kechengpingjia`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `kechengmingcheng` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '课程名称',
  `kechengleixing` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '课程类型',
  `kechengtupian` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '课程图片',
  `jiaoxuemubiao` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '教学目标',
  `jiaoxuefangfa` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '教学方法',
  `jiaoxueneirong` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '教学内容',
  `jiaoxuexiaoguo` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '教学效果',
  `pingjiashijian` date NULL DEFAULT NULL COMMENT '评价时间',
  `pingjiajianyi` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '评价建议',
  `yonghuzhanghao` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户账号',
  `yonghuxingming` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `sfsh` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '待审核' COMMENT '是否审核',
  `shhf` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '审核回复',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '课程评价' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of kechengpingjia
-- ----------------------------
INSERT INTO `kechengpingjia` VALUES (41, '2024-03-05 18:16:46', '课程名称1', '课程类型1', 'upload/kechengpingjia_kechengtupian1.jpg,upload/kechengpingjia_kechengtupian2.jpg,upload/kechengpingjia_kechengtupian3.jpg', 'A', 'A', 'A', 'A', '2024-03-05', '评价建议1', '用户账号1', '用户姓名1', '是', '');
INSERT INTO `kechengpingjia` VALUES (42, '2024-03-05 18:16:46', '课程名称2', '课程类型2', 'upload/kechengpingjia_kechengtupian2.jpg,upload/kechengpingjia_kechengtupian3.jpg,upload/kechengpingjia_kechengtupian4.jpg', 'A', 'A', 'A', 'A', '2024-03-05', '评价建议2', '用户账号2', '用户姓名2', '是', '');
INSERT INTO `kechengpingjia` VALUES (43, '2024-03-05 18:16:46', '课程名称3', '课程类型3', 'upload/kechengpingjia_kechengtupian3.jpg,upload/kechengpingjia_kechengtupian4.jpg,upload/kechengpingjia_kechengtupian5.jpg', 'A', 'A', 'A', 'A', '2024-03-05', '评价建议3', '用户账号3', '用户姓名3', '是', '');
INSERT INTO `kechengpingjia` VALUES (44, '2024-03-05 18:16:46', '课程名称4', '课程类型4', 'upload/kechengpingjia_kechengtupian4.jpg,upload/kechengpingjia_kechengtupian5.jpg,upload/kechengpingjia_kechengtupian6.jpg', 'A', 'A', 'A', 'A', '2024-03-05', '评价建议4', '用户账号4', '用户姓名4', '是', '');
INSERT INTO `kechengpingjia` VALUES (45, '2024-03-05 18:16:46', '课程名称5', '课程类型5', 'upload/kechengpingjia_kechengtupian5.jpg,upload/kechengpingjia_kechengtupian6.jpg,upload/kechengpingjia_kechengtupian7.jpg', 'A', 'A', 'A', 'A', '2024-03-05', '评价建议5', '用户账号5', '用户姓名5', '是', '');
INSERT INTO `kechengpingjia` VALUES (46, '2024-03-05 18:16:46', '课程名称6', '课程类型6', 'upload/kechengpingjia_kechengtupian6.jpg,upload/kechengpingjia_kechengtupian7.jpg,upload/kechengpingjia_kechengtupian8.jpg', 'A', 'A', 'A', 'A', '2024-03-05', '评价建议6', '用户账号6', '用户姓名6', '是', '');
INSERT INTO `kechengpingjia` VALUES (47, '2024-03-05 18:16:46', '课程名称7', '课程类型7', 'upload/kechengpingjia_kechengtupian7.jpg,upload/kechengpingjia_kechengtupian8.jpg,upload/kechengpingjia_kechengtupian9.jpg', 'A', 'A', 'A', 'A', '2024-03-05', '评价建议7', '用户账号7', '用户姓名7', '是', '');
INSERT INTO `kechengpingjia` VALUES (48, '2024-03-05 18:16:46', '课程名称8', '课程类型8', 'upload/kechengpingjia_kechengtupian8.jpg,upload/kechengpingjia_kechengtupian9.jpg,upload/kechengpingjia_kechengtupian10.jpg', 'A', 'A', 'A', 'A', '2024-03-05', '评价建议8', '用户账号8', '用户姓名8', '是', '');
INSERT INTO `kechengpingjia` VALUES (49, '2024-03-05 18:23:06', '课程名称1', '课程类型1', 'upload/kechengxinxi_kechengtupian1.jpg', 'B', 'C', 'A', 'B', '2024-03-05', 'XXXX', '用户账号2', '用户姓名2', '是', '审核用户评价');

-- ----------------------------
-- Table structure for learning_progress
-- ----------------------------
DROP TABLE IF EXISTS `learning_progress`;
CREATE TABLE `learning_progress`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `lesson_id` bigint NOT NULL COMMENT '课时ID',
  `progress_seconds` int NULL DEFAULT 0 COMMENT '学习进度（秒）',
  `is_completed` tinyint(1) NULL DEFAULT 0 COMMENT '是否已完成',
  `last_updated` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_user_lesson`(`user_id` ASC, `lesson_id` ASC) USING BTREE,
  INDEX `lesson_id`(`lesson_id` ASC) USING BTREE,
  CONSTRAINT `learning_progress_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `yonghu` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `learning_progress_ibfk_2` FOREIGN KEY (`lesson_id`) REFERENCES `lessons` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学习进度跟踪表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of learning_progress
-- ----------------------------

-- ----------------------------
-- Table structure for lessons
-- ----------------------------
DROP TABLE IF EXISTS `lessons`;
CREATE TABLE `lessons`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `chapter_id` bigint NOT NULL COMMENT '所属章节ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课时标题',
  `lesson_order` int NOT NULL DEFAULT 0 COMMENT '课时显示顺序',
  `lesson_type` enum('video','article','quiz') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'video' COMMENT '课时类型',
  `video_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '视频播放地址',
  `duration_seconds` int NULL DEFAULT NULL COMMENT '内容时长(秒)',
  `content_for_ai` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '用于AI问答的文本内容',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `chapter_id`(`chapter_id` ASC) USING BTREE,
  CONSTRAINT `lessons_ibfk_1` FOREIGN KEY (`chapter_id`) REFERENCES `chapters` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程课时表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lessons
-- ----------------------------
INSERT INTO `lessons` VALUES (2, 1, '1.1 - OSI七层模型', 1, 'video', 'http://example.com/video1.mp4', 600, '这是OSI七层模型的视频文字稿...', '2025-06-25 15:57:17');
INSERT INTO `lessons` VALUES (3, 1, '1.2 - 课后阅读：TCP/IP协议栈', 2, 'article', NULL, NULL, 'TCP/IP协议栈是互联网的核心...', '2025-06-25 15:57:17');
INSERT INTO `lessons` VALUES (4, 2, '2.1 - 信号与编码', 1, 'video', 'http://example.com/video2.mp4', 800, '关于物理层的信号与编码...', '2025-06-25 15:57:17');

-- ----------------------------
-- Table structure for messages
-- ----------------------------
DROP TABLE IF EXISTS `messages`;
CREATE TABLE `messages`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `userid` bigint NOT NULL COMMENT '留言人id',
  `username` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `avatarurl` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '头像',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '留言内容',
  `cpicture` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '留言图片',
  `reply` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '回复内容',
  `rpicture` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '回复图片',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '意见反馈' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of messages
-- ----------------------------
INSERT INTO `messages` VALUES (91, '2024-03-05 18:16:46', 1, '用户名1', 'upload/messages_avatarurl1.jpg', '留言内容1', 'upload/messages_cpicture1.jpg', '回复内容1', 'upload/messages_rpicture1.jpg');
INSERT INTO `messages` VALUES (92, '2024-03-05 18:16:46', 2, '用户名2', 'upload/messages_avatarurl2.jpg', '留言内容2', 'upload/messages_cpicture2.jpg', '回复内容2', 'upload/messages_rpicture2.jpg');
INSERT INTO `messages` VALUES (93, '2024-03-05 18:16:46', 3, '用户名3', 'upload/messages_avatarurl3.jpg', '留言内容3', 'upload/messages_cpicture3.jpg', '回复内容3', 'upload/messages_rpicture3.jpg');
INSERT INTO `messages` VALUES (94, '2024-03-05 18:16:46', 4, '用户名4', 'upload/messages_avatarurl4.jpg', '留言内容4', 'upload/messages_cpicture4.jpg', '回复内容4', 'upload/messages_rpicture4.jpg');
INSERT INTO `messages` VALUES (95, '2024-03-05 18:16:46', 5, '用户名5', 'upload/messages_avatarurl5.jpg', '留言内容5', 'upload/messages_cpicture5.jpg', '回复内容5', 'upload/messages_rpicture5.jpg');
INSERT INTO `messages` VALUES (96, '2024-03-05 18:16:46', 6, '用户名6', 'upload/messages_avatarurl6.jpg', '留言内容6', 'upload/messages_cpicture6.jpg', '回复内容6', 'upload/messages_rpicture6.jpg');
INSERT INTO `messages` VALUES (97, '2024-03-05 18:16:46', 7, '用户名7', 'upload/messages_avatarurl7.jpg', '留言内容7', 'upload/messages_cpicture7.jpg', '回复内容7', 'upload/messages_rpicture7.jpg');
INSERT INTO `messages` VALUES (98, '2024-03-05 18:16:46', 8, '用户名8', 'upload/messages_avatarurl8.jpg', '留言内容8', 'upload/messages_cpicture8.jpg', '回复内容8', 'upload/messages_rpicture8.jpg');
INSERT INTO `messages` VALUES (99, '2024-03-05 18:23:36', 12, '用户账号2', 'upload/yonghu_touxiang2.jpg', '<p>可以给管理反馈</p>', 'upload/1709634214625.jpg', '<p>回复反馈</p>', NULL);

-- ----------------------------
-- Table structure for storeup
-- ----------------------------
DROP TABLE IF EXISTS `storeup`;
CREATE TABLE `storeup`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `userid` bigint NOT NULL COMMENT '用户id',
  `refid` bigint NULL DEFAULT NULL COMMENT '商品id',
  `tablename` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '表名',
  `name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '名称',
  `picture` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '图片',
  `type` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '1' COMMENT '类型',
  `inteltype` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '推荐类型',
  `remark` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '收藏表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of storeup
-- ----------------------------
INSERT INTO `storeup` VALUES (1, '2024-03-05 18:22:18', 1709634123827, 21, 'kechengxinxi', '课程名称1', 'upload/kechengxinxi_kechengtupian1.jpg', '1', NULL, NULL);
INSERT INTO `storeup` VALUES (2, '2024-03-05 18:22:21', 1709634123827, 22, 'kechengxinxi', '课程名称2', 'upload/kechengxinxi_kechengtupian2.jpg', '1', NULL, NULL);
INSERT INTO `storeup` VALUES (3, '2024-03-05 18:22:32', 12, 21, 'kechengxinxi', '课程名称1', 'upload/kechengxinxi_kechengtupian1.jpg', '1', NULL, NULL);
INSERT INTO `storeup` VALUES (4, '2024-03-05 18:22:57', 12, 21, 'kechengxinxi', '课程名称1', 'upload/kechengxinxi_kechengtupian1.jpg', '21', NULL, NULL);
INSERT INTO `storeup` VALUES (5, '2024-03-05 18:23:20', 12, 61, 'forum', '帖子标题1', '1', '21', NULL, NULL);

-- ----------------------------
-- Table structure for syslog
-- ----------------------------
DROP TABLE IF EXISTS `syslog`;
CREATE TABLE `syslog`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `username` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `operation` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户操作',
  `method` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `params` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '请求参数',
  `time` bigint NULL DEFAULT NULL COMMENT '请求时长(毫秒)',
  `ip` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '系统日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of syslog
-- ----------------------------
INSERT INTO `syslog` VALUES (19, '2025-06-23 15:39:07', '111', '修改用户', 'com.controller.YonghuController.update()', '{\"id\":1709634123827,\"yonghuzhanghao\":\"111\",\"mima\":\"111\",\"yonghuxingming\":\"张三\",\"xingbie\":\"男\",\"dianhua\":\"13111111111\",\"touxiang\":\"upload/1750664341122.jpg\",\"addtime\":\"Mar 5, 2024 6:22:03 PM\"}', 8, '0:0:0:0:0:0:0:1');
INSERT INTO `syslog` VALUES (20, '2025-06-23 15:39:12', '111', '修改用户', 'com.controller.YonghuController.update()', '{\"id\":1709634123827,\"yonghuzhanghao\":\"111\",\"mima\":\"111\",\"yonghuxingming\":\"张三\",\"xingbie\":\"男\",\"dianhua\":\"13111111111\",\"touxiang\":\"upload/1750664341122.jpg\",\"addtime\":\"Mar 5, 2024 6:22:03 PM\"}', 6, '0:0:0:0:0:0:0:1');

-- ----------------------------
-- Table structure for token
-- ----------------------------
DROP TABLE IF EXISTS `token`;
CREATE TABLE `token`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint NOT NULL COMMENT '用户id',
  `username` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `tablename` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '表名',
  `role` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '角色',
  `token` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `expiratedtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = 'token表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of token
-- ----------------------------
INSERT INTO `token` VALUES (1, 1709634123827, '111', 'yonghu', '用户', 'tjh9jlkk7yoxx7x70mvnuh59sq3d0inr', '2024-03-05 18:22:07', '2025-06-25 18:22:51');
INSERT INTO `token` VALUES (2, 12, '用户账号2', 'yonghu', '用户', '8ir9fw4fbnhfj9vptcvv95c909fqvnoe', '2024-03-05 18:22:29', '2024-03-05 19:22:30');
INSERT INTO `token` VALUES (3, 1, 'admin', 'users', '管理员', 'yyvuc4fafhf1i7a9aupu3gosimrjgpcd', '2024-03-05 18:24:18', '2025-06-25 18:23:02');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `image` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '头像',
  `role` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '管理员' COMMENT '角色',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'admin', 'admin', 'upload/image1.jpg', '管理员', '2024-03-05 18:16:46');

-- ----------------------------
-- Table structure for yonghu
-- ----------------------------
DROP TABLE IF EXISTS `yonghu`;
CREATE TABLE `yonghu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `yonghuzhanghao` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户账号',
  `mima` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `yonghuxingming` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户姓名',
  `xingbie` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '性别',
  `dianhua` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '电话',
  `touxiang` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '头像',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `yonghuzhanghao`(`yonghuzhanghao` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1709634123828 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of yonghu
-- ----------------------------
INSERT INTO `yonghu` VALUES (11, '2024-03-05 18:16:45', '用户账号1', '123456', '用户姓名1', '男', '13823888881', 'upload/yonghu_touxiang1.jpg');
INSERT INTO `yonghu` VALUES (12, '2024-03-05 18:16:45', '用户账号2', '123456', '用户姓名2', '男', '13823888882', 'upload/yonghu_touxiang2.jpg');
INSERT INTO `yonghu` VALUES (13, '2024-03-05 18:16:45', '用户账号3', '123456', '用户姓名3', '男', '13823888883', 'upload/yonghu_touxiang3.jpg');
INSERT INTO `yonghu` VALUES (14, '2024-03-05 18:16:45', '用户账号4', '123456', '用户姓名4', '男', '13823888884', 'upload/yonghu_touxiang4.jpg');
INSERT INTO `yonghu` VALUES (15, '2024-03-05 18:16:45', '用户账号5', '123456', '用户姓名5', '男', '13823888885', 'upload/yonghu_touxiang5.jpg');
INSERT INTO `yonghu` VALUES (16, '2024-03-05 18:16:45', '用户账号6', '123456', '用户姓名6', '男', '13823888886', 'upload/yonghu_touxiang6.jpg');
INSERT INTO `yonghu` VALUES (17, '2024-03-05 18:16:45', '用户账号7', '123456', '用户姓名7', '男', '13823888887', 'upload/yonghu_touxiang7.jpg');
INSERT INTO `yonghu` VALUES (18, '2024-03-05 18:16:45', '用户账号8', '123456', '用户姓名8', '男', '13823888888', 'upload/yonghu_touxiang8.jpg');
INSERT INTO `yonghu` VALUES (1709634123827, '2024-03-05 18:22:03', '111', '111', '张三', '男', '13111111111', 'upload/1750664341122.jpg');

SET FOREIGN_KEY_CHECKS = 1;
