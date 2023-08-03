/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.common;

/**
 *
 * @author Administrator
 */
public final class Keyword {

    public static final String DIR = "dir";
    public static final String ID = "id";
    public static final String PARENT_ID = "parentId";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String VERSION = "version";
    public static final String ENABLE = "enable";
    public static final String PATH = "path";
    public static final String AWAYS_UPDATE = "awaysUpdate";
    public static final String PASSWORD = "password";
    public static final String COMMAND = "command";
    public static final String PC_NAME = "pcname";

    public static final class FilterName {

        public static final String LINE = "Line";
        public static final String STATION = "Station";
        public static final String PRODUCT = "Product";
    }

    public static final class Store {

        public static final String LOG_DIR = "store_log_dir";
        public static final String LOCAL_BACKUP = "store_local_backup";
        public static final String LOCAL_APP = "store_local_app";
        public static final String UPDATE_TIME = "store_update_time";
    }

    public static final class Url {

        public static final String LOGIN = "api.url.login";

        public static final class Product {

            public static final String GET = "api.url.product.get";
            public static final String POST = "api.url.product.post";
            public static final String DELETE = "api.url.product.delete";
            public static final String PUT = "api.url.product.put";
        }

        public static final class Station {

            public static final String GET = "api.url.station.get";
            public static final String POST = "api.url.station.post";
            public static final String DELETE = "api.url.station.delete";
            public static final String PUT = "api.url.station.put";
        }

        public static final class Line {

            public static final String GET = "api.url.line.get";
            public static final String POST = "api.url.line.post";
            public static final String DELETE = "api.url.line.delete";
            public static final String PUT = "api.url.line.put";
        }

        public static final class Pc {

            public static final String GET_APP_DOWNLOAD = "api.url.pc.get.app.download";
            public static final String GET_APP_INFO = "api.url.pc.get.app.info";
            public static final String GET = "api.url.pc.get";
            public static final String POST = "api.url.pc.post";
            public static final String DELETE = "api.url.pc.delete";
            public static final String PUT = "api.url.pc.put";
            public static final String PUT_INFO = "api.url.pc.put.info";
        }

        public static final class Fgroup {

            public static final String GET = "api.url.fgroup.get";
            public static final String GET_LIST = "api.url.fgroup.getlist";
            public static final String POST = "api.url.fgroup.post";
            public static final String DELETE = "api.url.fgroup.delete";
            public static final String PUT = "api.url.fgroup.put";
        }

        public static final class FileProgram {

            public static final String GET_LIST = "api.url.fprogram.getlist";
            public static final String GET = "api.url.fprogram.get";
            public static final String POST = "api.url.fprogram.post";
            public static final String DELETE = "api.url.fprogram.delete";
            public static final String PUT = "api.url.fprogram.put";
            public static final String GET_LAST_VERSION_DOWNLOAD = "api.url.fprogram.getversiondownload";
            public static final String GET_VERSION = "api.url.fprogram.getversion";
        }

        public static final class Program {

            public static final String GET_PCS = "api.url.program.get.pc";
            public static final String GET_FGROUP = "api.url.program.get.fgroup";
            public static final String GET_FGROUPS = "api.url.program.get.fgroups";
            public static final String GET_PROGRAMS = "api.url.program.get.fileprograms";
            public static final String GET_FILE_PROGRAM_VSERSION = "api.url.program.get.fileprogram.version";
            public static final String GET = "api.url.program.get";
            public static final String GET_LIST = "api.url.program.getlist";
            public static final String POST = "api.url.program.post";
            public static final String DELETE = "api.url.program.delete";
            public static final String PUT = "api.url.program.put";
            public static final String PUT_FGROUP = "api.url.program.put.fgroup";
            public static final String PUT_FILE_PROGRAM= "api.url.program.put.fprogram";
        }

        public static final class File {

            public static final String GET = "api.url.file.get";
            public static final String GET_LAST_VERSION_DOWNLOAD = "api.url.file.getversiondownload";
            public static final String GET_VERSION = "api.url.file.getversion";
            public static final String POST = "api.url.file.post";
            public static final String DELETE = "api.url.file.delete";
            public static final String PUT = "api.url.file.put";
        }
    }
}
