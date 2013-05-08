MC-Zone
=======

Official repository of the MC Zone servers.

- Use the "lib" folder as libraries for the plugins, and add any library necessary for new plugins.
- The "MC Zone" plugin should be used as an external library in the MC Zone plugins. It contains helpful tools and APIs to make development easier.


MC Zone Plugin
- Use Hive.getInstance() to access MC Zone's core functions.
- Set player variables with Hive.getInstance().getGamer(player).setVariable(String key, Object value)
- Query database with Hive.getInstance().getDatabase().query(string)
