/** When your routing system is too long, you can split it into small modules **/

import Layout from '@/layout'

const systemRouter = {
  path: '/system',
  component: Layout,
  redirect: 'noRedirect',
  name: '系统管理',
  alwaysShow: true,
  meta: { title: '系统管理', icon: 'system', permission: 'system:menu' },
  children: [
    {
      path: 'user',
      component: () => import('@/views/system/user'),
      name: '用户管理',
      meta: {
        title: '用户管理',
        icon: 'user-tie',
        permission: 'system:user:menu'
      }
    },
    {
      path: 'role',
      component: () => import('@/views/system/role'),
      name: '角色管理',
      meta: {
        title: '角色管理',
        icon: 'peoples',
        permission: 'system:role:menu'
      }
    },
    {
      path: 'resource',
      component: () => import('@/views/system/resource'),
      name: '资源管理',
      meta: {
        title: '资源管理',
        icon: 'resource',
        permission: 'system:resource:menu'
      }
    },
    {
      path: 'log',
      component: () => import('@/views/system/log'),
      name: '操作日志',
      meta: { title: '操作日志', icon: 'log', permission: 'system:log:menu' }
    }
  ]
}
export default systemRouter
