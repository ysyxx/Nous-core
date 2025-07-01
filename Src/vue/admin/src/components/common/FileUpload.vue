<template>
	<div>
		<el-upload v-if="type==1" ref="upload"
			:action="getActionUrl"         :auto-upload="false"           list-type="picture-card" :multiple="multiple" :limit="limit"
			:headers="myHeaders" :file-list="fileList" :on-exceed="handleExceed" :on-preview="handleUploadPreview"
			:on-remove="handleRemove" :on-success="handleUploadSuccess" :on-error="handleUploadErr"
			:before-upload="handleBeforeUpload"
            :on-change="handleFileChange"    >
			<i class="el-icon-plus"></i>
			<div slot="tip" class="el-upload__tip" style="color:#838fa1;">{{tip}}</div>
		</el-upload>
		<el-upload v-else drag ref="upload"
			:action="getActionUrl"         :auto-upload="false"           :multiple="multiple" :limit="limit"
			:headers="myHeaders" :file-list="fileList" :on-exceed="handleExceed" :on-preview="handleUploadPreview"
			:on-remove="handleRemove" :on-success="handleUploadSuccess" :on-error="handleUploadErr"
			:before-upload="handleBeforeUpload"
            :on-change="handleFileChange"     >
			<i class="el-icon-upload"></i>
			  <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
			<div slot="tip" class="el-upload__tip" style="color:#838fa1;">{{tip}}</div>
		</el-upload>
		</div>
</template>
<script>
	import storage from "@/utils/storage";
	import base from "@/utils/base";
	export default {
		data() {
			return {
				dialogVisible: false,
				dialogImageUrl: "",
				fileList: [],
				fileUrlList: [],
				myHeaders: {},
                selectedRawFile: null, // 新增：保存原始文件对象
			};
		},
		props: {
			tip: { type: String },
			action: { type: String }, // 这个 action 属性我们可以留着，但不再用于实际上传
			limit: { type: Number, default: 3 },
			multiple: { type: Boolean, default: false },
			fileUrls: { type: String },
			type: { type: Number, default: 1 }
		},
		mounted() {
			this.init();
			this.myHeaders = {
				'Token': storage.get("Token")
			}
		},
		watch: {
			fileUrls: function(val, oldVal) {
				this.init();
			}
		},
		computed: {
			// 修改 getActionUrl，使其返回一个空字符串，从而禁用 el-upload 的自动上传行为
			getActionUrl: function() {
				return ''; // 返回空字符串，防止 el-upload 发送请求
			}
		},
		methods: {
			init() {
				if (this.fileUrls) {
					this.fileUrlList = this.fileUrls.split(",");
					let fileArray = [];
					this.fileUrlList.forEach(function(item, index) {
						var url = item;
						var name = index + '_existing_file'; // 为现有文件设置一个不同的名称或标记
						var file = {
							name: name,
							url: url,
                            status: 'success' // 标记为已上传成功，防止 el-upload 显示上传中
						};
						fileArray.push(file);
					});
					this.setFileList(fileArray);
				} else {
                    this.fileList = []; // 如果没有 fileUrls，确保 fileList 是空的
                    this.fileUrlList = [];
                }
			},
            // 新增方法：处理文件选择事件，获取原始文件对象
            handleFileChange(file, fileList) {
                // 如果是多文件，这里需要处理逻辑
                if (this.multiple) {
                    // 对于多文件，你需要决定如何处理 fileList 并传递出去
                    // 比如，你可以传递整个 fileList.map(f => f.raw) 数组
                    this.selectedRawFile = fileList.map(f => f.raw);
                    // 同时，如果你要显示预览，需要手动设置 fileList
                    this.fileList = fileList; // 确保内部 fileList 更新以显示图片
                } else {
                    // 单文件上传，只关心最新的文件
                    if (file.status === 'ready') { // 确保是文件已选择但未上传的状态
                         this.selectedRawFile = file.raw; // 保存原始 File 对象
                         // 为预览而更新 fileList
                         this.fileList = [file];
                    } else if (file.status === 'removed') {
                         this.selectedRawFile = null;
                         this.fileList = [];
                    }
                }
                // 重要：向父组件发出事件，传递原始文件对象
                this.$emit("change", this.selectedRawFile); // 传递原始文件对象
                // 注意：这里不再需要处理 fileUrlList，因为不再是自动上传
            },
            // 移除图片 (需要调整以正确更新 selectedRawFile)
            handleRemove(file, fileList) {
                // 如果 file.raw 存在，说明是新选择的文件被移除了
                if (file.raw) {
                    // 对于多文件，需要从 selectedRawFile 数组中移除
                    if (this.multiple) {
                        const index = this.selectedRawFile.findIndex(f => f === file.raw);
                        if (index > -1) {
                            this.selectedRawFile.splice(index, 1);
                        }
                    } else {
                        this.selectedRawFile = null;
                    }
                }
                // 同时更新内部 fileList 以移除预览
                this.fileList = fileList;
                // 通知父组件文件列表变化，传递更新后的原始文件或文件数组
                this.$emit("change", this.multiple ? this.selectedRawFile : null);
            },
            // 这些方法在禁用自动上传后将不再被 el-upload 自动调用，可以移除或留作备用
            handleBeforeUpload(file) {
                 // 这里可以做文件类型和大小校验，如果校验不通过返回 false
                 // 例如：
                 // const isJPGorPNG = file.type === 'image/jpeg' || file.type === 'image/png';
                 // const isLt2M = file.size / 1024 / 1024 < 2;
                 // if (!isJPGorPNG) {
                 //   this.$message.error('上传图片只能是 JPG/PNG 格式!');
                 // }
                 // if (!isLt2M) {
                 //   this.$message.error('上传图片大小不能超过 2MB!');
                 // }
                 // return isJPGorPNG && isLt2M;
                 return true; // 总是返回 true，因为我们不在这里阻止上传，只是校验
            },
            handleUploadSuccess(res, file, fileList) {
                // 这个方法在禁用 auto-upload 后将不再被触发
                // 如果你的 file-upload 仍然有其他上传机制，这里可能有用
            },
            handleUploadErr(err, file, fileList) {
                // 这个方法在禁用 auto-upload 后将不再被触发
            },

			// 查看大图 (这个方法保留，因为它是预览功能)
			handleUploadPreview(file) {
				if(this.type > 2){ // 如果type > 2，表示是其他文件类型，直接打开
					window.open(file.url)
					return false
				}
                // 对于新选择的文件，需要使用本地 URL 预览
                if (file.raw && file.url.startsWith('blob:')) {
                    this.dialogImageUrl = file.url;
                } else { // 对于已有文件（通过 fileUrls 初始化），使用其真实 URL
                    this.dialogImageUrl = file.url.split("?")[0]; // 移除 token
                }
				this.dialogVisible = true;
			},
			// 限制图片数量
			handleExceed(files, fileList) {
				this.$message.warning(`最多上传${this.limit}张图片`);
			},
			// 重新对fileList进行赋值 (主要用于初始化和移除操作的内部状态管理)
            // 在不自动上传模式下，这个方法主要用于管理 el-upload 内部的 fileList 状态，
            // 以及处理 fileUrls 初始化的逻辑。
			setFileList(fileList) {
				var fileArray = [];
				var fileUrlArray = []; // 这个列表可能不再需要，因为我们现在传递原始文件
				var token = storage.get("token");
				let _this = this;
				fileList.forEach(function(item, index) {
                    // 如果是原始文件（用户新选的），它的 url 是 blob:URL
                    // 如果是已有的文件（通过 fileUrls 传入），它的 url 是真实路径
                    let url = item.url;
                    // 只对非 blob URL 添加 token
					if (!url.startsWith("http") && !url.startsWith("blob:")) {
						url = _this.$base.url + url
					}
					var name = item.name || `file-${index}`;
					var file = {
						name: name,
						url: url + (url.includes('?') ? '&' : '?') + "token=" + token, // 确保 token 正确拼接
                        status: item.status || 'success', // 保持原有状态
                        raw: item.raw // 保持原始文件引用
					};
					fileArray.push(file);
					if (!item.raw) { // 只有非原始文件才添加到 fileUrlArray (如果还需要这个数组)
					    fileUrlArray.push(url);
					}
				});
				this.fileList = fileArray;
				this.fileUrlList = fileUrlArray; // 这个现在主要用于 `fileUrls` 初始化时的处理
			},
			     
		}
	};
</script>
<style lang="scss" scoped>
</style>