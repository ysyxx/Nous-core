<template>
  <div class="ai-assistant-container">
    <!-- 3D悬浮按钮 -->
    <div class="ai-assistant" @click="toggleChat" @mouseenter="hoverButton = true" @mouseleave="hoverButton = false">
      <div class="icon-container" :class="{ 'hover-effect': hoverButton }">
        <div class="icon-bg"></div>
        <img src="@/assets/ai-assistant-icon.png" alt="AI Assistant" class="ai-icon" />
        <div class="pulse-ring"></div>
      </div>
      <transition name="tooltip-fade">
        <div v-if="hoverButton" class="tooltip-box">
          <span>点击咨询智能助手</span>
          <div class="tooltip-arrow"></div>
        </div>
      </transition>
    </div>

    <!-- 聊天窗口 -->
    <transition name="chat-scale">
      <div v-if="showChat" class="chat-box" :class="{ fullscreen: isFullscreen }">
        <div class="chat-header">
          <div class="header-content">
            <div class="avatar">
              <img src="@/assets/ai-avatar.png" alt="AI Avatar" />
            </div>
            <div class="header-text">
              <h3>智能助手</h3>
              <p>24小时为您服务</p>
            </div>
          </div>
          <div class="header-actions">
            <button class="action-btn close-btn" @click="closeChat">
              <span>×</span>
            </button>
          </div>
        </div>

        <div class="chat-content" ref="chatContent">
          <div v-for="(msg, index) in messages" :key="index" :class="['message', msg.sender]">
            <div class="message-bubble">
              <div class="message-text" v-html="msg.text"></div>
              <div class="message-meta">
                <span class="message-time">{{ formatTime(msg.time) }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="input-container">
          <div class="input-box-wrapper">
            <textarea
                placeholder="输入消息..."
                v-model="newMessage"
                @keydown.enter="handleEnter"
            ></textarea>
            <button class="send-btn-embedded" @click="sendMessage">
              <svg width="24" height="24" viewBox="0 0 24 24">
                <path d="M2 21l21-9L2 3v7l15 2-15 2v7z" fill="currentColor" />
              </svg>
            </button>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import { marked } from 'marked'
import hljs from 'highlight.js'
import 'highlight.js/styles/github.css' // 代码高亮样式

// 配置 marked 支持换行和代码高亮
marked.setOptions({
  breaks: true, // 支持换行符转成 <br>
  highlight: function (code, lang) {
    if (lang && hljs.getLanguage(lang)) {
      return hljs.highlight(code, { language: lang }).value
    }
    return hljs.highlightAuto(code).value
  }
})

// 新增：markdown自动修复函数
function sanitizeMarkdown(md) {
  // 块级元素前补空行
  let fixed = md
      // 标题前补空行
      .replace(/([^\n])(\n#+ )/g, '$1\n\n$2')
      // 列表前补空行
      .replace(/([^\n])(\n[-*] )/g, '$1\n\n$2')
      // 有序列表前补空行
      .replace(/([^\n])(\n\d+\. )/g, '$1\n\n$2')
      // 引用前补空行
      .replace(/([^\n])(\n> )/g, '$1\n\n$2');
  // 列表后跟非列表内容时补两个换行
  fixed = fixed.replace(/((?:^|\n)(?:[-*] |\d+\. ).+)(\n(?![-*] |\d+\. |$))/g, (m, p1, p2) => {
    // 如果下一行不是列表、不是空行，则补一个换行
    return p1 + '\n' + p2;
  });
  // 列表块后跟非列表块时再补一个换行
  fixed = fixed.replace(/((?:^|\n)(?:[-*] |\d+\. ).+(?:\n(?:[-*] |\d+\. ).+)*)\n(?![-*] |\d+\. |\n|$)/g, '$1\n\n');
  return fixed;
}

export default {
  data() {
    return {
      showChat: false,
      hoverButton: false,
      messages: [
        {
          text: '您好！我是您的智能助手<span class="wave-hand">👋</span>，有什么可以帮您解决的问题吗？',
          sender: 'ai',
          time: new Date()
        }
      ],
      newMessage: '',
      aiTyping: false,
      isFullscreen: false,
    }
  },
  watch: {
    messages: {
      handler() {
        this.$nextTick(() => {
          this.scrollToBottom()
        })
      },
      deep: true,
    },
  },
  methods: {
    toggleChat() {
      this.showChat = !this.showChat
    },
    closeChat() {
      this.showChat = false
    },
    handleEnter(e) {
      if (!e.shiftKey) {
        e.preventDefault()
        this.sendMessage()
      }
    },
    scrollToBottom() {
      const container = this.$refs.chatContent
      if (container) {
        container.scrollTop = container.scrollHeight
      }
    },
    formatTime(date) {
      return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
    },

    async sendMessage() {
      const input = this.newMessage.trim()
      if (!input) return

      // 用户消息
      const userMsg = {
        text: input,
        sender: 'user',
        time: new Date()
      }
      this.messages.push(userMsg)
      this.newMessage = ''

      this.aiTyping = true

      const aiMsg = {
        text: '<span class="typing">AI 正在拼命思考🤔...</span>',
        sender: 'ai',
        time: new Date()
      }
      this.messages.push(aiMsg)

      try {
        const token = localStorage.getItem('frontToken') || ''
        const payload = {
          model: 'moonshot-v1-8k',
          messages: [{ role: 'user', content: input }]
        }

        const response = await fetch('http://localhost:9000/api/ai/stream_ask', {
          method: 'POST',
          headers: {
            Token: token,
            'Content-Type': 'application/json',
            Accept: 'text/event-stream',
          },
          body: JSON.stringify(payload),
        })

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`)
        }

        const reader = response.body.getReader()
        const decoder = new TextDecoder('utf-8')
        let buffer = ''
        let markdownBuffer = ''
        let firstChunk = true

        while (true) {
          const { done, value } = await reader.read()
          if (done) break

          buffer += decoder.decode(value, { stream: true })
          const lines = buffer.split('\n')
          buffer = lines.pop()

          for (let i = 0; i < lines.length; i += 2) {
            const line = lines[i]
            if (line.startsWith('data: ')) {
              const content = line.slice(6)
              if (content === '[DONE]') {
                // 先修复 markdown，再渲染
                aiMsg.text = marked.parse(sanitizeMarkdown(markdownBuffer))
                this.aiTyping = false
                return
              }
              if (firstChunk) {
                markdownBuffer = ''
                firstChunk = false
              }
              markdownBuffer += content
              aiMsg.text = marked.parse(sanitizeMarkdown(markdownBuffer))
            } else {
              markdownBuffer += '\n'
              aiMsg.text = marked.parse(sanitizeMarkdown(markdownBuffer))
            }
          }
        }
        this.aiTyping = false
      } catch (err) {
        console.error('AI 请求失败:', err)
        aiMsg.text = '❌ 抱歉，AI 服务暂时不可用。'
        this.aiTyping = false
      }
    },
  },
}
</script>

<style scoped>

/* 基础样式 */
:root {
  --primary-color: #4361ee;
  --primary-light: #4cc9f0;
  --secondary-color: #3a0ca3;
  --text-dark: #2b2d42;
  --text-light: #f8f9fa;
  --bg-light: #ffffff;
  --shadow-color: rgba(0,0,0,0.1);
}

/* 3D悬浮按钮 */
.ai-assistant {
  position: fixed;
  right: 40px;
  bottom: 40px;
  cursor: pointer;
  z-index: 1000;
  perspective: 1000px;
}

.icon-container {
  position: relative;
  width: 80px;
  height: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}

.icon-container.hover-effect {
  transform: translateY(-5px) rotateY(15deg);
}

.icon-bg {
  position: absolute;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #2a4bd7, #3a0ca3);
  border-radius: 50%;
  box-shadow: 0 10px 30px rgba(42, 75, 215, 0.4);
}

.ai-icon {
  width: 50px;
  height: 50px;
  object-fit: contain;
  z-index: 2;
  filter: drop-shadow(0 2px 4px rgba(0,0,0,0.2));
  transition: transform 0.3s ease;
}

.icon-container:hover .ai-icon {
  transform: scale(1.1);
}

.pulse-ring {
  position: absolute;
  width: 100%;
  height: 100%;
  border: 2px solid var(--primary-light);
  border-radius: 50%;
  animation: pulse 2s infinite;
  opacity: 0;
}

@keyframes pulse {
  0% {
    transform: scale(0.9);
    opacity: 0.7;
  }
  70% {
    transform: scale(1.3);
    opacity: 0;
  }
  100% {
    opacity: 0;
  }
}

/* 提示框 */

.tooltip-box {
  position: absolute;
  right: 100%;
  top: 50%;
  transform: translateY(-50%);
  margin-right: 15px;
  padding: 10px 16px;
  background: linear-gradient(135deg, #4361ee, #3a0ca3);
  color: white;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 500;
  white-space: nowrap;
  box-shadow: 0 4px 20px rgba(67, 97, 238, 0.3);
  opacity: 0.95;
  transition: all 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}

.tooltip-box::before {
  content: '';
  position: absolute;
  left: 100%;
  top: 50%;
  transform: translateY(-50%);
  border-width: 8px;
  border-style: solid;
  border-color: transparent transparent transparent #3a0ca3;
}

.tooltip-fade-enter-active {
  transition: opacity 0.3s ease-out, transform 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}
.tooltip-fade-leave-active {
  transition: opacity 0.2s ease-in, transform 0.2s ease-in;
}
.tooltip-fade-enter-from,
.tooltip-fade-leave-to {
  opacity: 0;
  transform: translateY(-50%) translateX(-10px);
}


/* 聊天窗口 */
.chat-box {
  position: fixed;
  right: 40px;
  bottom: 130px;
  width: 380px;
  height: 600px;
  border-radius: 16px;
  background: var(--bg-light);
  box-shadow: 0 15px 50px rgba(0,0,0,0.2);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  z-index: 1001;
  transform-origin: bottom right;
}

.chat-header {
  padding: 0 20px;
  height: 80px;
  background: white;
  color: rgb(235, 235, 235);
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.header-content {
  display: flex;
  align-items: center;
}

.avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 12px;
  border: 2px solid rgba(255,255,255,0.3);
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.header-text h3 {
  margin: 0;
  font-weight: 600;
  font-size: 18px;
  color: #2b2d42;
}

.header-text p {
  margin: 3px 0 0;
  font-size: 12px;
  opacity: 0.8;
  color: #2b2d42;
}

.header-actions {
  display: flex;
}

.action-btn {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: transparent;
  border: none;
  color: #2b2d42;;
  font-size: 20px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.2s;
}

.action-btn:hover {
  background: rgba(255,255,255,0.1);
}

.close-btn {
  /* 布局与尺寸 */
  position: absolute; /* 根据需要调整定位 */
  top: 20px; /* 根据需要调整 */
  right: 20px; /* 根据需要调整 */
  width: 30px; /* 宽度调整为适合小圆圈的大小 */
  height: 30px; /* 高度与宽度相同，形成圆形 */
  padding: 0; /* 移除内边距 */
  border: none; /* 移除边框 */
  background-color: #888888; /* 灰色背景 */
  border-radius: 50%; /* 圆角设置为50%，形成圆形 */
  color: white; /* 文字颜色，确保×符号可见 */
  font-size: 16px; /* 文字大小，适用于×符号 */
  font-weight: bold; /* 文字加粗，增强×符号的可视性 */
  cursor: pointer; /* 鼠标悬停时显示手形指针 */
  display: flex; /* 使用flex布局使×符号居中 */
  align-items: center; /* 垂直居中 */
  justify-content: center; /* 水平居中 */
  transition: background-color 0.3s ease; /* 添加背景色过渡效果 */
  outline: none; /* 移除按钮默认轮廓线 */
}

.close-btn:hover {
  background-color: #666666; /* 鼠标悬停时背景色变深 */
}

.close-btn span {
  /* 确保×符号居中且不会溢出按钮 */
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}


/* 聊天内容区 */
.chat-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background: #f5f7fb;
  background-image:
      linear-gradient(rgba(245,247,251,0.9), rgba(245,247,251,0.9)),
      url('data:image/svg+xml;utf8,<svg width="100" height="100" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg"><path d="M30 30L70 70M70 30L30 70" stroke="%23e0e5f0" stroke-width="0.5"/></svg>');
}

.message {
  margin-bottom: 20px;
  display: flex;
}

.message.user {
  justify-content: flex-end;
}

.message-bubble {
  max-width: 80%;
  position: relative;
}

.message-text {
  padding: 12px 16px;
  border-radius: 18px;
  font-size: 15px;
  line-height: 1.4;
  word-break: break-word;
}

.message.ai .message-text {
  background: white;
  color: var(--text-dark);
  border-top-left-radius: 4px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.message.user .message-text {
  background-color: #1890ff;
  color: white;
  border-bottom-right-radius: 4px;
}

.message-meta {
  display: flex;
  align-items: center;
  margin-top: 5px;
  font-size: 11px;
  color: #6c757d;
}

.message.user .message-meta {
  justify-content: flex-end;
}

.message-time {
  opacity: 0.7;
}

.message-status {
  margin-left: 5px;
  color: #4cc9f0;
}

/* 输入区域 */
.chat-input-area {
  padding: 15px;
  border-top: 1px solid #e9ecef;
  background: white;
  display: flex;
  align-items: flex-end;
}

.input-tools {
  display: flex;
  flex-direction: column;
  margin-right: 10px;
}

.tool-btn {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: transparent;
  border: none;
  color: #2b2d42;;
  font-size: 18px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.tool-btn:hover {
  background: #f1f3f5;
  color: var(--primary-color);
}


/* 主容器 */
.input-container {
  padding: 16px;
  background: white;
  border-top: 1px solid #f1f3f5;
}

/* 输入框容器 - 关键相对定位容器 */
.input-box-wrapper {
  position: relative;
  display: flex;
  border-radius: 24px;
  background: #f8f9fa;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  transition: all 0.3s ease;
}

/* 输入框聚焦状态 */
.input-box-wrapper:focus-within {
  background: #e9ecef;
  box-shadow: 0 0 0 2px rgba(67, 97, 238, 0.2);
}

/* 文本输入区域 */
.input-box-wrapper textarea {
  width: 100%;
  border: none;
  resize: none;
  padding: 14px 60px 14px 20px; /* 右侧留出按钮空间 */
  background: transparent;
  font-size: 15px;
  line-height: 1.5;
  max-height: 150px;
  outline: none;
  border-radius: 24px; /* 确保与容器一致 */
}

/* 嵌入式发送按钮 */
.send-btn-embedded {
  position: absolute;
  right: 8px;
  top: 50%;
  transform: translateY(-50%);
  width: 40px;
  height: 40px;
  border-radius: 24px; /* 与输入框匹配 */
  background: var(--primary-color, #4361ee);
  border: none;
  color: white;
  font-size: 18px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.send-btn-embedded:disabled {
  background: #adb5bd;
  cursor: not-allowed;
}

.send-btn-embedded:not(:disabled):hover {
  background: var(--secondary-color, #3a56e0);
  transform: translateY(-50%) scale(1.05);
}

/* 确保聚焦时输入框和按钮一起变色 */
.input-box-wrapper:focus-within .send-btn-embedded {
  background: var(--secondary-color, #3a56e0);
}

/* 动画效果 */
.chat-scale-enter-active, .chat-scale-leave-active {
  transition: all 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}
.chat-scale-enter, .chat-scale-leave-to {
  opacity: 0;
  transform: scale(0.8) translateY(20px);
}

/* 特殊样式 */
.wave-hand {
  display: inline-block;
  animation: wave 1.5s infinite;
  transform-origin: 70% 70%;
}

@keyframes wave {
  0% { transform: rotate(0deg); }
  10% { transform: rotate(14deg); }
  20% { transform: rotate(-8deg); }
  30% { transform: rotate(14deg); }
  40% { transform: rotate(-4deg); }
  50% { transform: rotate(10deg); }
  60% { transform: rotate(0deg); }
  100% { transform: rotate(0deg); }
}

.typing-dots span {
  display: inline-block;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: currentColor;
  margin: 0 2px;
  opacity: 0.4;
  animation: typing 1.4s infinite both;
}

.typing-dots span:nth-child(1) {
  animation-delay: 0s;
}
.typing-dots span:nth-child(2) {
  animation-delay: 0.2s;
}
.typing-dots span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 100% {
    opacity: 0.4;
    transform: translateY(0);
  }
  50% {
    opacity: 1;
    transform: translateY(-3px);
  }
}

/* 滚动条美化 */
.chat-content::-webkit-scrollbar {
  width: 6px;
}
.chat-content::-webkit-scrollbar-track {
  background: rgba(0,0,0,0.05);
}
.chat-content::-webkit-scrollbar-thumb {
  background: rgba(0,0,0,0.2);
  border-radius: 3px;
}
.chat-content::-webkit-scrollbar-thumb:hover {
  background: rgba(0,0,0,0.3);
}

/* 新增全屏样式 */
.chat-box {
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  /* 原有样式... */
}

/* 移除全屏样式 */
.chat-box {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%); /* 精确居中关键 */
  width: 80vw;
  height: 70vh;
  max-width: 900px;
  border-radius: 12px;
  z-index: 1000;
  box-shadow: 0 10px 30px rgba(0,0,0,0.15);
  transition: all 0.3s ease;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .chat-box {
    width: 90vw;
    height: 80vh;
  }
}

/* 移除全屏状态覆盖 */
.chat-box.fullscreen {
  /* 保留类名但清空样式 */
  all: unset;
  /* 重新应用基础样式 */
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 80vw;
  height: 70vh;
  max-width: 900px;
  border-radius: 12px;
  z-index: 1000;
}

</style>
