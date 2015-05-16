package sk.tomsik68.mclauncher.impl.versions.mcassets;

import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Enumeration;

/**
 * Class loader that allows us to add URL to classpath and doesn't take META-INF folder into account
 */
@Deprecated
final class CustomClassLoader extends URLClassLoader {
    public CustomClassLoader() {
        this(new URL[0], CustomClassLoader.class.getClassLoader());
    }

    public CustomClassLoader(URL[] arg0, ClassLoader parent) {
        super(arg0);
    }

    public void addJAR(URL url) {
        addURL(url);
    }

    protected PermissionCollection getPermissions(CodeSource codesource) {
        PermissionCollection pc = new PermissionCollection() {
            public boolean implies(Permission permission) {
                return true;
            }

            public Enumeration<Permission> elements() {
                return null;
            }

            public void add(Permission permission) {
            }
        };
        return pc;
    }

    public URL getResource(String arg0) {
        if (arg0.contains("META-INF"))
            return null;
        return super.getResource(arg0);
    }

    public Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}