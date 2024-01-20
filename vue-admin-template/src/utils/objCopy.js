/**
 * 复制对象
 * @param {object} obj1 - 要复制的对象
 * @param {object} obj2 - 复制后的对象
 */
export default async function objCopy(obj1, obj2) {
    Object.keys(obj2).forEach(key => {
        obj2[key] = obj1[key]
    })
}