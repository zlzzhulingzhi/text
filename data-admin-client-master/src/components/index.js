import Vue from 'vue'
// 树形选择器
import TreeSelect from '@riophae/vue-treeselect'

Vue.component('SvgIcon', () => import('./common/SvgIcon'))
const requireAll = requireContext => requireContext.keys().map(requireContext);
/**
 * require.context(directory,useSubdirectories,regExp)
 * require.context():需要一次性的引入某个文件夹下的所有文件
 形参：
 directory：需要引入文件的目录
 useSubdirectories：是否查找该目录下的子级目录
 regExp：匹配引入文件的正则表达式
 */
const req = require.context('@/assets/svg', false, /\.svg$/);
requireAll(req);

Vue.component('Captcha', () => import('./common/Captcha'))
Vue.component('DynamicBackground', () => import('./common/DynamicBackground'))
Vue.component('Results', () => import('./common/Results'))
Vue.component('RouterAlive', () => import('./common/RouterAlive'))
Vue.component('Breadcrumb', () => import('./common/Breadcrumb'))
Vue.component('Banner', () => import('./common/Banner'))
Vue.component('ICPInFooter', () => import('./common/ICPInFooter'))
Vue.component('ToolTip', () => import('./common/ToolTip'))



Vue.component('EleDot', () => import('./element/EleDot'))
Vue.component('EleEnabledSwitch', () => import('./element/EleEnabledSwitch'))
Vue.component('EleIcon', () => import('./element/EleIcon'))


Vue.component('TableView', () => import('./pages/TableView'))
Vue.component('UploadImage', () => import('./upload/UploadImage'))


Vue.component('TreeSelect', TreeSelect)

Vue.component('TreeSelectQ', () => import('./form/TreeSelectQ'))
Vue.component('VisibleUserList', () => import('./form/VisibleUserList'))
Vue.component('FormSaveBar', () => import('./form/FormSaveBar'))
Vue.component("ButtonSave", () => import("./form/ButtonSave"));
Vue.component('FormSaveBarInDialog', () => import('./form/FormSaveBarInDialog'))
